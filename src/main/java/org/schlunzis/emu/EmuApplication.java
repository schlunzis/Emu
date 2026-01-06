package org.schlunzis.emu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.schlunzis.emu.device.ByteConsumer;
import org.schlunzis.emu.device.CharacterDevice;
import org.schlunzis.emu.device.CharacterDeviceException;
import org.schlunzis.emu.device.MessageConsumer;
import org.schlunzis.emu.view.MainView;
import org.schlunzis.jduino.protocol.tlv.TLV;

import java.io.IOException;

public class EmuApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 320, 240);
        stage.setTitle("Emuuuuuuuuuu");
        stage.setScene(scene);
        stage.show();

        Thread.ofVirtual().start(() -> {
            CharacterDevice device = new CharacterDevice();
            TLV tlv = new TLV();
            ByteConsumer consumer = new MessageConsumer<>(tlv);
            device.setByteConsumer(consumer);
            try {
                device.initialize();
            } catch (CharacterDeviceException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
