package org.schlunzis.emu.model.protocol;

public record EchoMessage(
        String message
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
