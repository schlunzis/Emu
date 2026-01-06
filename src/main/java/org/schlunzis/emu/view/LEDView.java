package org.schlunzis.emu.view;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import lombok.Getter;

@Getter
public class LEDView extends HBox {

    private final TextField pinField = new TextField();
    private final Circle led = new Circle(10);

    public LEDView() {
        pinField.setPromptText("Pin");
        pinField.setPrefWidth(50);

        this.getChildren().addAll(pinField, led);
    }

}
