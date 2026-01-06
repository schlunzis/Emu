package org.schlunzis.emu.model.protocol;

import org.schlunzis.jduino.protocol.Message;
import org.schlunzis.jduino.protocol.MessageEncoder;

public class CustomMessageEncoder implements MessageEncoder<CustomProtocol> {
    @Override
    public byte[] encode(Message<CustomProtocol> message) {
        return new byte[0];
    }
}
