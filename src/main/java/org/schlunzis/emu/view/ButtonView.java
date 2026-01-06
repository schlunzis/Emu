package org.schlunzis.emu.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class ButtonView extends HBox {

    private final PinField pinField = new PinField();
    private final Button button = new Button("Button");

    public ButtonView() {
        this.getChildren().addAll(button, pinField);
    }

}
