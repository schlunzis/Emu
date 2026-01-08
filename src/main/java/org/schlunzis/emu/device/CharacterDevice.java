package org.schlunzis.emu.device;

import org.schlunzis.emu.model.protocol.CustomProtocol;
import org.schlunzis.jduino.channel.Channel;
import org.schlunzis.jduino.channel.ChannelMessageListener;
import org.schlunzis.jduino.channel.Device;
import org.schlunzis.jduino.channel.DeviceConfiguration;
import org.schlunzis.jduino.channel.serial.SerialDevice;
import org.schlunzis.jduino.protocol.Message;
import org.schlunzis.jduino.protocol.MessageEncoder;

import java.io.IOException;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.foreign.ValueLayout.*;

public class CharacterDevice implements Channel<CustomProtocol> {

    private final CustomProtocol protocol;
    private final List<ChannelMessageListener<CustomProtocol>> messageConsumers = new ArrayList<>();
    private final List<ByteConsumer> rxByteListener = new ArrayList<>();
    private final List<ByteConsumer> txBbyteListener = new ArrayList<>();

    private String device;
    private int masterFd;

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

    public CharacterDevice(CustomProtocol protocol) {
        this.protocol = protocol;
    }

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

            device = name.getString(0);
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
                    for (int i = 0; i < n; i++) {
                        byte b = buffer.get(JAVA_BYTE, i);
                        rxByteListener.forEach(l -> l.accept(b));
                        protocol.getMessageDecoder().pushNextByte(b);
                        if (protocol.getMessageDecoder().isMessageComplete()) {
                            Message<CustomProtocol> message = protocol.getMessageDecoder().getDecodedMessage();
                            for (ChannelMessageListener<CustomProtocol> consumer : messageConsumers) {
                                consumer.onMessageReceived(message);
                            }
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
            for (byte b : data)
                for (ByteConsumer c : txBbyteListener)
                    c.accept(b);
        } catch (Throwable e) {
            throw new IOException("Error writing to device", e);
        }
    }

    @Override
    public void open(DeviceConfiguration deviceConfiguration) {
        try {
            initialize();
        } catch (CharacterDeviceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        // Nothing to do
    }

    @Override
    public void sendMessage(Message<CustomProtocol> message) {
        MessageEncoder<CustomProtocol> encoder = protocol.getMessageEncoder();
        try {
            byte[] encoded = encoder.encode(message);
            write(encoded);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    @Override
    public List<? extends Device> getDevices() {
        return List.of(new SerialDevice("Emu Device", device));
    }

    @Override
    public void addMessageListener(ChannelMessageListener<CustomProtocol> channelMessageListener) {
        messageConsumers.add(channelMessageListener);
    }

    @Override
    public void removeMessageListener(ChannelMessageListener<CustomProtocol> channelMessageListener) {
        messageConsumers.remove(channelMessageListener);
    }

    public void addRxByteListener(ByteConsumer consumer) {
        rxByteListener.add(consumer);
    }

    public void addTxByteListener(ByteConsumer consumer) {
        txBbyteListener.add(consumer);
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
