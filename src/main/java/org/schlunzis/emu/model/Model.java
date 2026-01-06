package org.schlunzis.emu.model;

import org.schlunzis.jduino.protocol.Protocol;

public class Model {

    private final Protocol<?> protocol;

    public Model(Protocol<?> protocol) {
        this.protocol = protocol;
    }

}
