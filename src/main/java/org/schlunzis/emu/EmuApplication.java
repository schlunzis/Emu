package org.schlunzis.emu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.schlunzis.emu.controller.ButtonController;
import org.schlunzis.emu.controller.ExtensibleListController;
import org.schlunzis.emu.controller.LEDController;
import org.schlunzis.emu.controller.MainController;
import org.schlunzis.emu.device.ByteConsumer;
import org.schlunzis.emu.device.CharacterDevice;
import org.schlunzis.emu.device.CharacterDeviceException;
import org.schlunzis.emu.device.MessageConsumer;
import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.model.protocol.CustomProtocol;
import org.schlunzis.emu.view.ButtonView;
import org.schlunzis.emu.view.LEDView;
import org.schlunzis.emu.view.MainView;
import org.schlunzis.jduino.protocol.tlv.TLV;

public class EmuApplication extends Application {

    @Override
    public void start(Stage stage) {
        CustomProtocol protocol = new CustomProtocol();
        Model model = new Model(protocol);
        MainController mainController = createViewController(model);
        MainView mainView = mainController.getMainView();
        Scene scene = new Scene(mainView, 320, 240);
        stage.setTitle("Emuuuuuuuuuu");
        stage.setScene(scene);
        stage.show();

        Thread.ofVirtual().start(() -> {
            CharacterDevice device = new CharacterDevice(protocol);
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

    private MainController createViewController(Model model) {
        MainView mainView = new MainView();

        new ExtensibleListController<>(mainView.getInteractionView().getButtonListView()) {
            @Override
            protected ButtonView createNewElement() {
                ButtonView buttonView = new ButtonView();
                new ButtonController(buttonView, model);
                return buttonView;
            }
        };
        new ExtensibleListController<>(mainView.getInteractionView().getLedListView()) {
            @Override
            protected LEDView createNewElement() {
                LEDView ledView = new LEDView();
                new LEDController(ledView, model);
                return ledView;
            }
        };

        return new MainController(mainView);
    }

}
