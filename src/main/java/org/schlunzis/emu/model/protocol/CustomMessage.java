package org.schlunzis.emu.model.protocol;

import org.schlunzis.jduino.protocol.Message;

public sealed interface CustomMessage extends Message<CustomProtocol> permits ACKMessage, ButtonMessage, EchoMessage, LCDMessage, LEDMessage {
}
