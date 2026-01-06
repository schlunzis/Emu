package org.schlunzis.emu.view;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class ButtonView extends HBox {

    private final TextField pinField = new TextField();
    private final Button button = new Button("Button");

    public ButtonView() {
        pinField.setPromptText("Pin");
        pinField.setPrefWidth(50);
        this.getChildren().addAll(button, pinField);
    }

}
