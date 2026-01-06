package org.schlunzis.emu.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class ExtensibleListView<T extends Node> extends VBox {

    private final ListView<T> listView = new ListView<>();
    private final Button addButton = new Button("+");

    public ExtensibleListView() {
        this.getChildren().addAll(listView, addButton);
    }

}
