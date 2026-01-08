package org.schlunzis.emu.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.schlunzis.emu.controller.EchoController;

@Getter
public class EchoView extends VBox {

    private final ListView<EchoController.EchoListItem> echoListView = new ListView<>();
    private final TextField inputField = new TextField();
    private final Button sendButton = new Button(">");

    public EchoView() {
        HBox inputArea = new HBox();
        inputArea.getChildren().addAll(inputField, sendButton);

        VBox.setVgrow(echoListView, Priority.ALWAYS);

        this.getChildren().addAll(echoListView, inputArea);
    }


}
