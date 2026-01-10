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
            case ButtonMessage btn -> new TLVMessage(SimpleChannel.CMD_BUTTON, btn.getPayload());
            case LEDMessage led -> new TLVMessage(SimpleChannel.CMD_LED, led.getPayload());
            case EchoMessage echo -> new TLVMessage(SimpleChannel.CMD_ECHO, echo.getPayload());
            case ACKMessage ack -> new TLVMessage((byte) 0, ack.getPayload());
            default -> throw new IllegalArgumentException("Unknown message type: " + message.getClass().getName());
        };
        return tlvMessageEncoder.encode(m);
    }

    private byte booleanToByte(boolean b) {
        return (byte) (b ? 1 : 0);
    }

}
