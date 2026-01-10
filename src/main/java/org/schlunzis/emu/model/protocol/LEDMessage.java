package org.schlunzis.emu.model.protocol;

public record LEDMessage(
        int pin,
        boolean state
) implements CustomMessage {
    @Override
    public byte getMessageType() {
        return 3;
    }

    @Override
    public byte[] getPayload() {
        return new byte[]{
            (byte) pin,
            (byte) (state ? 1 : 0)
        };
    }
}
