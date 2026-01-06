package org.schlunzis.emu.model.protocol;

public record LEDMessage(
        int pin,
        boolean state
) implements CustomMessage {
    @Override
    public byte getMessageType() {
        return 0;
    }

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }
}
