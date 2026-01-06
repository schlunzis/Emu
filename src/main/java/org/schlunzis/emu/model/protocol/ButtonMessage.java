package org.schlunzis.emu.model.protocol;

public record ButtonMessage(
        int pin
) implements CustomMessage {
    @Override
    public byte getMessageType() {
        return 1;
    }

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }
}
