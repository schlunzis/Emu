package org.schlunzis.emu.model.protocol;

import org.schlunzis.jduino.protocol.Message;
import org.schlunzis.jduino.protocol.MessageDecoder;
import org.schlunzis.jduino.protocol.tlv.TLVMessage;
import org.schlunzis.jduino.protocol.tlv.TLVMessageDecoder;
import org.schlunzis.jduino.simple.SimpleChannel;

public class CustomMessageDecoder implements MessageDecoder<CustomProtocol> {

    private final TLVMessageDecoder tlvMessageDecoder = new TLVMessageDecoder();

    @Override
    public void pushNextByte(byte b) {
        tlvMessageDecoder.pushNextByte(b);
    }

    @Override
    public boolean isMessageComplete() {
        return tlvMessageDecoder.isMessageComplete();
    }

    @Override
    public Message<CustomProtocol> getDecodedMessage() {
        TLVMessage tlvMessage = tlvMessageDecoder.getDecodedMessage();
        return switch (tlvMessage) {
            case TLVMessage(byte type, byte[] value) when type == SimpleChannel.CMD_BUTTON ->
                    new ButtonMessage(value[0]);
            case TLVMessage(byte type, byte[] value) when type == SimpleChannel.CMD_LED ->
                    new LEDMessage(value[0], byteToBoolean(value[1]));
            case TLVMessage(byte type, byte[] value) when type == SimpleChannel.CMD_ECHO ->
                    new EchoMessage(new String(value));
            default -> throw new IllegalArgumentException("Unknown message type: " + tlvMessage.getMessageType());
        };
    }

    private boolean byteToBoolean(byte b) {
        return b != 0;
    }

}
