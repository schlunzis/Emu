package org.schlunzis.emu.view;

import javafx.scene.control.SplitPane;
import lombok.Getter;

@Getter
public class InteractionView extends SplitPane {

    private final ExtensibleListView<ButtonView> buttonListView = new ExtensibleListView<>();
    private final ExtensibleListView<LEDView> ledListView = new ExtensibleListView<>();
    private final EchoView echoView = new EchoView();

    public InteractionView() {
        this.getItems().addAll(buttonListView, ledListView, echoView);
    }

}
