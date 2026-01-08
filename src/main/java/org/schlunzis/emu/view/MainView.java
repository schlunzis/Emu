package org.schlunzis.emu.view;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import lombok.Getter;

@Getter
public class MainView extends SplitPane {

    private final ProtocolView protocolView = new ProtocolView();
    private final SplitPane rightSide = new SplitPane();
    private final InteractionView interactionView = new InteractionView();
    private final RxTxView rxTxView = new RxTxView();

    public MainView() {
        this.getItems().addAll(protocolView, rightSide);
        rightSide.getItems().addAll(interactionView, rxTxView);
        rightSide.orientationProperty().set(Orientation.VERTICAL);
    }

}
