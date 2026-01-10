package org.schlunzis.emu.controller;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.model.protocol.ACKMessage;
import org.schlunzis.emu.model.protocol.CustomMessage;
import org.schlunzis.emu.model.protocol.EchoMessage;
import org.schlunzis.emu.view.EchoView;

import java.util.List;

public class EchoController {

    private final EchoView echoView;
    private final Model model;

    public EchoController(EchoView echoView, Model model) {
        this.echoView = echoView;
        this.model = model;

        echoView.getEchoListView().setCellFactory(listView -> {
            ListCell<EchoListItem> cell = new ListCell<>() {

                private final TextFlow textFlow = new TextFlow();

                @Override
                protected void updateItem(EchoListItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        textFlow.getChildren().clear();
                        textFlow.getChildren().addAll(formatMessage(item));
                        setGraphic(textFlow);
                    }
                }
            };
            cell.prefWidthProperty().bind(listView.widthProperty().subtract(30));
            return cell;
        });

        echoView.getSendButton().setOnAction(_ -> {
            String text = echoView.getInputField().getText();
            if (text != null && !text.isBlank()) {
                CustomMessage message = new EchoMessage(text);
                model.send(message);
                echoView.getEchoListView().getItems().add(new EchoListItem(EchoItemDirection.SENT, text));
                echoView.getInputField().clear();
            }
        });

        model.addListener(message -> {
                    if (message instanceof EchoMessage(String m)) {
                        Platform.runLater(() ->
                                echoView.getEchoListView().getItems().add(new EchoListItem(EchoItemDirection.RECEIVED, m))
                        );
                        model.send(new ACKMessage("Echo: " + m));
                    }
                }
        );
    }

    private List<Text> formatMessage(EchoListItem item) {
        return switch (item.direction) {
            case SENT -> {
                Text prefix = new Text("← ");
                prefix.setStyle("-fx-font-weight: bold; -fx-fill: blue;");
                Text message = new Text(item.message);
                message.setStyle("-fx-fill: black;");
                yield List.of(prefix, message);
            }
            case RECEIVED -> {
                Text prefix = new Text("→ ");
                prefix.setStyle("-fx-font-weight: bold; -fx-fill: green;");
                Text message = new Text(item.message);
                message.setStyle("-fx-fill: black;");
                yield List.of(prefix, message);
            }
        };
    }

    public enum EchoItemDirection {
        SENT,
        RECEIVED
    }

    public record EchoListItem(
            EchoItemDirection direction,
            String message
    ) {
    }

}
