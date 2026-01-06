package org.schlunzis.emu.controller;

import javafx.scene.Node;
import org.schlunzis.emu.view.ExtensibleListView;

public abstract class ExtensibleListController<T extends Node> {

    private final ExtensibleListView<T> extensibleListView;

    protected ExtensibleListController(ExtensibleListView<T> extensibleListView) {
        this.extensibleListView = extensibleListView;

        this.extensibleListView.getAddButton().setOnAction(_ -> {
            T newElement = createNewElement();
            addElement(newElement);
        });
    }

    protected abstract T createNewElement();

    private void addElement(T element) {
        extensibleListView.getListView().getItems().add(element);
    }

}
