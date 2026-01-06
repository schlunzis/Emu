package org.schlunzis.emu.controller;

import lombok.Getter;
import org.schlunzis.emu.view.MainView;

public class MainController {

    @Getter
    private final MainView mainView;

    public MainController(MainView mainView) {
        this.mainView = mainView;
    }

}
