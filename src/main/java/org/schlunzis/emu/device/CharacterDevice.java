package org.schlunzis.emu.device;

import java.io.*;

public class CharacterDevice {

    private OutputStream toClient;
    private ByteConsumer byteConsumer;

    public void initialize() throws IOException {
        Process helper = new ProcessBuilder("target/pty-helper")
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

        BufferedReader helperOut = new BufferedReader(new InputStreamReader(helper.getInputStream()));

        // First line = slave PTY path
        String device = helperOut.readLine();
        System.out.println("Client should open: " + device);

        InputStream fromClient = helper.getInputStream();
        toClient = helper.getOutputStream();

        int b;
        while ((b = fromClient.read()) != -1 && !Thread.currentThread().isInterrupted()) {
            if (byteConsumer != null) {
                byteConsumer.accept((byte) b);
            }
        }
    }

    public void writeByte(byte b) throws IOException {
        toClient.write(b);
        toClient.flush();
    }

    public void writeString(String s) throws IOException {
        toClient.write(s.getBytes());
        toClient.flush();
    }

    public void setByteConsumer(ByteConsumer byteConsumer) {
        this.byteConsumer = byteConsumer;
    }

}
