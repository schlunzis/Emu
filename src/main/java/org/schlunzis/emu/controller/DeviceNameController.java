package org.schlunzis.emu.controller;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import lombok.Getter;
import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.view.DeviceNameView;

@Getter
public class DeviceNameController {

    private final DeviceNameView deviceNameView;
    private final Model model;

    public DeviceNameController(DeviceNameView view, Model model) {
        this.deviceNameView = view;
        this.model = model;

        deviceNameView.getCopyToClipboardButton().setOnAction(_ -> {
            String deviceName = deviceNameView.getDeviceNameLabel().getText();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(deviceName);
            clipboard.setContent(content);
        });
    }

    public void updateDeviceName() {
        deviceNameView.getDeviceNameLabel().setText(model.getDeviceName());
    }

}
