package org.schlunzis.emu.model.protocol;

import java.nio.charset.StandardCharsets;

public record ACKMessage(String message) implements CustomMessage {
    @Override
    public byte getMessageType() {
        return 0;
    }

    @Override
    public byte[] getPayload() {
        return message.getBytes(StandardCharsets.UTF_8);
    }
}
