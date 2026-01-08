package org.schlunzis.emu.controller;

import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.view.ControlView;

public class ControlController {

    public ControlController(ControlView controlView, Model model, DeviceNameController deviceNameController) {
        controlView.getStartButton().setOnAction(_ -> deviceNameController.updateDeviceName());
    }

}
