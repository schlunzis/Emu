package org.schlunzis.emu.controller;

import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.model.protocol.LEDMessage;
import org.schlunzis.emu.view.LEDView;

public class LEDController {

    private final LEDView ledView;
    private final Model model;

    public LEDController(LEDView ledView, Model model) {
        this.ledView = ledView;
        this.model = model;

        this.model.addListener(msg -> {
            if (msg instanceof LEDMessage(int pin, boolean state) && pin == getLEDPin()) {
                setLED(state);
            }
        });
    }

    public int getLEDPin() {
        return ledView.getPinField().getPinNumber();
    }

    public void setLED(boolean on) {
        if (on) {
            ledView.getLed().setStyle("-fx-fill: yellow;");
        } else {
            ledView.getLed().setStyle("-fx-fill: gray;");
        }
    }
}
