package org.schlunzis.emu.model.protocol;

import java.nio.charset.StandardCharsets;

public record EchoMessage(
        String message
) implements CustomMessage {
    @Override
    public byte getMessageType() {
        return 1;
    }

    @Override
    public byte[] getPayload() {
        return message.getBytes(StandardCharsets.UTF_8);
    }
}
