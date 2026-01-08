package org.schlunzis.emu.controller;

import javafx.application.Platform;
import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.view.RxTxView;

public class RxTxController {

    private final StringBuilder rxBuffer = new StringBuilder();
    private final StringBuilder txBuffer = new StringBuilder();

    public RxTxController(RxTxView rxTxView, Model model) {
        model.addRxByteListener(byteData -> addToBuffer(rxBuffer, byteData));
        model.addListener(_ -> {
            String data = emptyBuffer(rxBuffer);
            Platform.runLater(() -> {
                if (!rxTxView.getRxArea().getText().isEmpty())
                    rxTxView.getRxArea().appendText("\n");
                rxTxView.getRxArea().appendText(data);
            });
        });

        model.addTxByteListener(byteData -> addToBuffer(txBuffer, byteData));
        model.addMessageSentListener(() -> {
            String data = emptyBuffer(txBuffer);
            Platform.runLater(() -> {
                if (!rxTxView.getTxArea().getText().isEmpty())
                    rxTxView.getTxArea().appendText("\n");
                rxTxView.getTxArea().appendText(data);
            });
        });

        // https://stackoverflow.com/a/20568196/13670629
        rxTxView.getRxArea().textProperty().addListener((_, _, _) ->
                rxTxView.getRxArea().setScrollTop(Double.MAX_VALUE));
        rxTxView.getTxArea().textProperty().addListener((_, _, _) ->
                rxTxView.getTxArea().setScrollTop(Double.MAX_VALUE));

    }

    private void addToBuffer(StringBuilder buffer, byte data) {
        String text = Integer.toHexString(data & 0xFF);
        if (text.length() < 2) {
            text = "0" + text;
        }
        text = text.toUpperCase();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (buffer) {
            buffer.append(text);
        }
    }

    private String emptyBuffer(StringBuilder buffer) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (buffer) {
            String data = buffer.toString();
            buffer.setLength(0);
            return data;
        }
    }

}
