package org.schlunzis.emu.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class DeviceNameView extends HBox {

    private final Label deviceNameLabel = new Label();
    private final Button copyToClipboardButton = new Button("Copy");

    public DeviceNameView() {
        this.getChildren().addAll(deviceNameLabel, copyToClipboardButton);
        this.alignmentProperty().set(Pos.CENTER);
        this.spacingProperty().set(10);
    }

}
