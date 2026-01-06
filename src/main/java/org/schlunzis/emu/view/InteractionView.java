package org.schlunzis.emu.view;

import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class InteractionView extends HBox {

    private final ExtensibleListView<ButtonView> buttonListView = new ExtensibleListView<>();
    private final ExtensibleListView<LEDView> ledListView = new ExtensibleListView<>();

    public InteractionView() {
        this.getChildren().addAll(buttonListView, ledListView);
    }

}
