package org.schlunzis.emu.model;

import org.schlunzis.emu.device.CharacterDevice;
import org.schlunzis.emu.model.protocol.CustomMessage;
import org.schlunzis.emu.model.protocol.CustomProtocol;
import org.schlunzis.jduino.protocol.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Model {

    private final Protocol<CustomProtocol> protocol;
    private final CharacterDevice characterDevice;

    private final List<Consumer<CustomMessage>> listeners = new ArrayList<>();

    public Model(Protocol<CustomProtocol> protocol, CharacterDevice characterDevice) {
        this.protocol = protocol;
        this.characterDevice = characterDevice;
        characterDevice.addMessageListener(m -> {
            if (m instanceof CustomMessage cm) {
                System.out.println("Model received message: " + cm);
                for (Consumer<CustomMessage> listener : listeners) {
                    listener.accept(cm);
                }
            }
        });
    }

    public void addListener(Consumer<CustomMessage> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<CustomMessage> listener) {
        listeners.remove(listener);
    }

    public void send(CustomMessage message) {
        System.out.println("Model sending message: " + message);
        characterDevice.sendMessage(message);
    }

}
