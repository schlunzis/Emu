package org.schlunzis.emu.view;

import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import lombok.Getter;

@Getter
public class LEDView extends HBox {

    private final PinField pinField = new PinField();
    private final Circle led = new Circle(10);

    public LEDView() {
        this.getChildren().addAll(pinField, led);
    }

}
