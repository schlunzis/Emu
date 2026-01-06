package org.schlunzis.emu.device;

import java.io.IOException;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.*;

public class CharacterDevice {

    private int masterFd;
    private ByteConsumer byteConsumer;

    private static final Linker LINKER = Linker.nativeLinker();
    private static final SymbolLookup LIBC = Linker.nativeLinker().defaultLookup();

    // int openpty(int*, int*, char*, void*, void*)
    private static final MethodHandle openpty = LINKER.downcallHandle(
            LIBC.find("openpty").orElseThrow(),
            FunctionDescriptor.of(JAVA_INT,
                    ADDRESS, ADDRESS, ADDRESS, ADDRESS, ADDRESS));

    private static final MethodHandle tcgetattr = LINKER.downcallHandle(
            LIBC.find("tcgetattr").orElseThrow(),
            FunctionDescriptor.of(JAVA_INT, JAVA_INT, ADDRESS));

    private static final MethodHandle tcsetattr = LINKER.downcallHandle(
            LIBC.find("tcsetattr").orElseThrow(),
            FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT, ADDRESS));

    private static final MethodHandle cfmakeraw = LINKER.downcallHandle(
            LIBC.find("cfmakeraw").orElseThrow(),
            FunctionDescriptor.ofVoid(ADDRESS));

    private static final MethodHandle read = LINKER.downcallHandle(
            LIBC.find("read").orElseThrow(),
            FunctionDescriptor.of(JAVA_LONG, JAVA_INT, ADDRESS, JAVA_LONG));

    private static final MethodHandle write = LINKER.downcallHandle(
            LIBC.find("write").orElseThrow(),
            FunctionDescriptor.of(JAVA_LONG, JAVA_INT, ADDRESS, JAVA_LONG));

    public void initialize() throws CharacterDeviceException {
        try (Arena arena = Arena.ofConfined()) {

            // int master, slave;
            MemorySegment master = arena.allocate(JAVA_INT);
            MemorySegment slave = arena.allocate(JAVA_INT);

            // char name[128]
            MemorySegment name = arena.allocate(128);

            int rc = (int) openpty.invoke(
                    master, slave, name,
                    MemorySegment.NULL,
                    MemorySegment.NULL);

            if (rc != 0) {
                throw new RuntimeException("openpty failed");
            }

            masterFd = master.get(JAVA_INT, 0);
            int slaveFd = slave.get(JAVA_INT, 0);

            String device = name.getString(0);
            System.out.println("Client should open: " + device);

            // termios struct (size is platform-dependent; 60 bytes works on Linux x86_64)
            MemorySegment termios = arena.allocate(60);

            tcgetattr.invoke(slaveFd, termios);
            cfmakeraw.invoke(termios);
            tcsetattr.invoke(slaveFd, 0 /* TCSANOW */, termios);

            MemorySegment buffer = arena.allocate(256);

            while (!Thread.currentThread().isInterrupted()) {
                long n = (long) read.invoke(masterFd, buffer, 256L);
                if (n > 0) {
                    if (byteConsumer != null) {
                        for (int i = 0; i < n; i++) {
                            byte b = buffer.get(JAVA_BYTE, i);
                            System.out.println("RX: " + Integer.toHexString(b));
                            byteConsumer.accept(b);
                        }
                    }
                    buffer.fill((byte) 0);
                }
            }
        } catch (Throwable t) {
            throw new CharacterDeviceException("Error initializing CharacterDevice", t);
        }
    }

    public void writeByte(byte b) throws IOException {
        write(new byte[]{b});
    }

    public void writeString(String s) throws IOException {
        write(s.getBytes(StandardCharsets.UTF_8));
    }

    private void write(byte[] data) throws IOException {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment out = arena.allocateFrom(JAVA_BYTE, data);
            long written = (long) write.invoke(masterFd, out, (long) data.length);
            if (written != data.length) {
                throw new IOException("Failed to write all bytes to device");
            }
        } catch (Throwable e) {
            throw new IOException("Error writing to device", e);
        }
    }

    public void setByteConsumer(ByteConsumer byteConsumer) {
        this.byteConsumer = byteConsumer;
    }

}
