package org.schlunzis.emu.view;

import eu.mihosoft.monacofx.MonacoFX;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class ProtocolView extends VBox {

    private final ControlView controlView = new ControlView();
    private final MonacoFX textArea = new MonacoFX();

    public ProtocolView() {
        textArea.getEditor().getDocument().setText("Write your protocol here...");
        this.getChildren().addAll(controlView, textArea);
    }

}
