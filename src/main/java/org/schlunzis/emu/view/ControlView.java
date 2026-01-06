package org.schlunzis.emu.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;


public class ControlView extends HBox {

    private final Button resetButton = new Button("Reset");
    private final Button startButton = new Button("Start");


    public ControlView() {
        this.getChildren().addAll(resetButton, startButton);
    }

}
