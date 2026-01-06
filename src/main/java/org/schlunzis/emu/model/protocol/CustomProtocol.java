package org.schlunzis.emu.model.protocol;

import org.schlunzis.jduino.protocol.MessageDecoder;
import org.schlunzis.jduino.protocol.MessageEncoder;
import org.schlunzis.jduino.protocol.Protocol;

public class CustomProtocol implements Protocol<CustomProtocol> {
    @Override
    public MessageEncoder<CustomProtocol> getMessageEncoder() {
        return null;
    }

    @Override
    public MessageDecoder<CustomProtocol> getMessageDecoder() {
        return null;
    }
}
