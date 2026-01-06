package org.schlunzis.emu.device;

import org.schlunzis.jduino.protocol.Message;
import org.schlunzis.jduino.protocol.MessageDecoder;
import org.schlunzis.jduino.protocol.Protocol;

import java.util.Arrays;

public class MessageConsumer<P extends Protocol<P>> implements ByteConsumer {

    private final MessageDecoder<P> messageDecoder;

    public MessageConsumer(Protocol<P> protocol) {
        this.messageDecoder = protocol.getMessageDecoder();
    }

    @Override
    public void accept(byte b) {
        messageDecoder.pushNextByte(b);
        if (messageDecoder.isMessageComplete()) {
            Message<P> message = messageDecoder.getDecodedMessage();
            System.out.println("Received complete message: " + Arrays.toString(message.getPayload()));
        }
    }
}
