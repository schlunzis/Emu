package org.schlunzis.emu.view;

import javafx.scene.control.TextField;

public class PinField extends TextField {

    public PinField() {
        setPromptText("Pin");
        setPrefWidth(50);

        textProperty().addListener((_, _, newValue) -> {
            if (!newValue.matches("\\d*")) {
                setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    public byte getPinNumber() {
        String text = getText();
        if (text.isEmpty()) {
            return -1; // or any other default value indicating no pin is set
        }
        int pinNumber = Integer.parseInt(text);
        if (pinNumber < 0 || pinNumber > 255) {
            throw new IllegalArgumentException("Pin number must be between 0 and 255");
        }
        return (byte) pinNumber;
    }

}
