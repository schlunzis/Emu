package org.schlunzis.emu.view;

import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class RxTxView extends SplitPane {

    private final TextArea rxArea = new TextArea();
    private final TextArea txArea = new TextArea();

    public RxTxView() {
        rxArea.setEditable(false);
        txArea.setEditable(false);

        VBox rxBox = new VBox();
        Label rxLabel = new Label("Received Messages");
        rxBox.getChildren().add(rxLabel);
        rxBox.getChildren().addAll(rxArea);
        VBox.setVgrow(rxArea, Priority.ALWAYS);

        VBox txBox = new VBox();
        Label txLabel = new Label("Transmitted Messages");
        txBox.getChildren().add(txLabel);
        txBox.getChildren().addAll(txArea);
        VBox.setVgrow(txArea, Priority.ALWAYS);

        this.getItems().addAll(txBox, rxBox);
        this.setDividerPositions(0.5);
    }

}
