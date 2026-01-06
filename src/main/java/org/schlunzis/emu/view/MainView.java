package org.schlunzis.emu.view;

import javafx.scene.control.SplitPane;
import lombok.Getter;

@Getter
public class MainView extends SplitPane {

    private final InteractionView interactionView = new InteractionView();
    private final ProtocolView protocolView = new ProtocolView();

    public MainView() {
        this.getItems().addAll(protocolView, interactionView);
    }

}
