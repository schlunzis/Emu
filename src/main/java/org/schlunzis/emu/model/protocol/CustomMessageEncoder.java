package org.schlunzis.emu.model.protocol;

import org.schlunzis.jduino.protocol.Message;
import org.schlunzis.jduino.protocol.MessageEncoder;
import org.schlunzis.jduino.protocol.tlv.TLVMessage;
import org.schlunzis.jduino.protocol.tlv.TLVMessageEncoder;
import org.schlunzis.jduino.simple.SimpleChannel;

public class CustomMessageEncoder implements MessageEncoder<CustomProtocol> {

    private final TLVMessageEncoder tlvMessageEncoder = new TLVMessageEncoder();

    @Override
    public byte[] encode(Message<CustomProtocol> message) {
        TLVMessage m = switch (message) {
            case ButtonMessage(byte buttonId) -> new TLVMessage(SimpleChannel.CMD_BUTTON, new byte[]{buttonId});
            case LEDMessage(byte ledId, boolean state) ->
                    new TLVMessage(SimpleChannel.CMD_LED, new byte[]{ledId, booleanToByte(state)});
            case EchoMessage(String text) -> new TLVMessage(SimpleChannel.CMD_ECHO, text.getBytes());
            default -> throw new IllegalArgumentException("Unknown message type: " + message.getClass().getName());
        };
        return tlvMessageEncoder.encode(m);
    }

    private byte booleanToByte(boolean b) {
        return (byte) (b ? 1 : 0);
    }

}
