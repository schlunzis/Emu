package org.schlunzis.emu.controller;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import jfx.incubator.scene.control.richtext.model.StyleAttributeMap;
import jfx.incubator.scene.control.richtext.model.StyledInput;
import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.view.RxTxView;

import java.util.ArrayList;
import java.util.List;

public class RxTxController {

    private final Buffer rxBuffer = new Buffer();
    private final Buffer txBuffer = new Buffer();

    private static final StyleAttributeMap PLAIN_STYLE = StyleAttributeMap.builder()
            .setTextColor(Color.DARKGRAY)
            .build();

    public RxTxController(RxTxView rxTxView, Model model) {
        model.addRxByteListener(byteData -> addToBuffer(rxBuffer, byteData));
        model.addListener(_ -> {
            String hexString;
            String plainString;
            synchronized (rxBuffer) {
                hexString = rxBuffer.getAsHexString();
                plainString = rxBuffer.getAsString();
                rxBuffer.clear();
            }
            Platform.runLater(() -> {
                if (rxTxView.getRxArea().getParagraphCount() != 0)
                    rxTxView.getRxArea().appendText("\n");

                StyledInput styledHexString = StyledInput.of(hexString);
                StyledInput styledPlainString = StyledInput.of(plainString, PLAIN_STYLE);
                rxTxView.getRxArea().appendText(styledHexString);
                rxTxView.getRxArea().appendText(styledPlainString);
            });
        });

        model.addTxByteListener(byteData -> addToBuffer(txBuffer, byteData));
        model.addMessageSentListener(() -> {
            String hexString;
            String plainString;
            synchronized (txBuffer) {
                hexString = txBuffer.getAsHexString();
                plainString = txBuffer.getAsString();
                txBuffer.clear();
            }
            Platform.runLater(() -> {
                if (!rxTxView.getTxArea().getText().isEmpty())
                    rxTxView.getTxArea().appendText("\n");
                rxTxView.getTxArea().appendText(hexString);
                rxTxView.getTxArea().appendText(plainString);
            });
        });

        // https://stackoverflow.com/a/20568196/13670629
        rxTxView.getTxArea().textProperty().addListener((_, _, _) ->
                rxTxView.getTxArea().setScrollTop(Double.MAX_VALUE));

    }

    private void addToBuffer(Buffer buffer, byte data) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (buffer) {
            buffer.add(data);
        }
    }

    private static class Buffer {
        private final List<Byte> byteList = new ArrayList<>();

        public void add(byte data) {
            byteList.add(data);
        }

        private String getAsHexString() {
            StringBuilder sb = new StringBuilder();
            for (byte b : byteList) {
                String text = Integer.toHexString(b & 0xFF);
                if (text.length() < 2) {
                    text = "0" + text;
                }
                sb.append(text.toUpperCase());
            }
            return sb.toString();
        }

        private String getAsString() {
            StringBuilder sb = new StringBuilder();
            for (byte b : byteList) {
                char ch = (char) b;
                if (!Character.isISOControl(ch)) {
                    sb.append(ch);
                }
            }
            return sb.toString();
        }

        public void clear() {
            byteList.clear();
        }
    }

}
