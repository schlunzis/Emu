package org.schlunzis.emu.controller;

import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.view.LEDView;

public class LEDController {

    private final LEDView ledView;
    private final Model model;

    public LEDController(LEDView ledView, Model model) {
        this.ledView = ledView;
        this.model = model;
    }

}
