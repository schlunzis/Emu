package org.schlunzis.emu.model.protocol;

public record LCDMessage(
        int pin,
        String message
) implements CustomMessage {
    @Override
    public byte getMessageType() {
        return 2;
    }

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }
}
