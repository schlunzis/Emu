package org.schlunzis.emu.device;

import java.io.IOException;

public class PlainConsumer implements ByteConsumer {

    private final CharacterDevice device;

    private final StringBuilder buffer = new StringBuilder();

    public PlainConsumer(CharacterDevice device) {
        this.device = device;
    }

    public void accept(byte b) {
        char c = (char) b;
        if (c == '\n') {
            String input = buffer.toString();
            System.out.println("RX: " + input);

            String response = "Echo: " + input + "\n";
            System.out.print("TX: '" + response + "'");
            try {
                device.writeString(response);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to write response to device");
            }

            buffer.setLength(0);
            return;
        }
        buffer.append((char) b);
    }

}
