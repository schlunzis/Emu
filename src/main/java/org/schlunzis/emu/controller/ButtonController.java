package org.schlunzis.emu.controller;

import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.model.protocol.ButtonMessage;
import org.schlunzis.emu.view.ButtonView;

public class ButtonController {

    private final ButtonView buttonView;
    private final Model model;

    public ButtonController(ButtonView buttonView, Model model) {
        this.buttonView = buttonView;
        this.model = model;

        this.buttonView.getButton().setOnAction(_ -> {
            int pin = getButtonPin();
            model.send(new ButtonMessage(pin));
        });
    }

    public int getButtonPin() {
        return buttonView.getPinField().getPinNumber();
    }

}
