package org.schlunzis.emu.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class ControlView extends HBox {

    private final Button resetButton = new Button("Reset");
    private final Button startButton = new Button("Start");
    private final DeviceNameView deviceNameView = new DeviceNameView();

    public ControlView() {
        this.getChildren().addAll(resetButton, startButton, deviceNameView);
        this.spacingProperty().set(10);
    }

}
