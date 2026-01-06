package org.schlunzis.emu.model.protocol;

import org.schlunzis.jduino.protocol.Message;
import org.schlunzis.jduino.protocol.MessageDecoder;

public class CustomMessageDecoder implements MessageDecoder<CustomProtocol> {
    @Override
    public void pushNextByte(byte b) {
        
    }

    @Override
    public boolean isMessageComplete() {
        return false;
    }

    @Override
    public Message<CustomProtocol> getDecodedMessage() {
        return null;
    }
}
