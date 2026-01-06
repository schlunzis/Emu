package org.schlunzis.emu.device;

import org.schlunzis.jduino.channel.Device;
import org.schlunzis.jduino.channel.DeviceConfiguration;

public record CharacterDeviceConfiguration(
        int baudRate
) implements DeviceConfiguration {

    @Override
    public Device getDevice() {
        return null;
    }

}
