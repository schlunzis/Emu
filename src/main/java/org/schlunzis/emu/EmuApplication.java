package org.schlunzis.emu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.schlunzis.emu.controller.*;
import org.schlunzis.emu.device.CharacterDevice;
import org.schlunzis.emu.device.CharacterDeviceException;
import org.schlunzis.emu.model.Model;
import org.schlunzis.emu.model.protocol.CustomProtocol;
import org.schlunzis.emu.view.ButtonView;
import org.schlunzis.emu.view.LEDView;
import org.schlunzis.emu.view.MainView;

public class EmuApplication extends Application {

    @Override
    public void start(Stage stage) {
        CustomProtocol protocol = new CustomProtocol();
        CharacterDevice device = new CharacterDevice(protocol);
        Model model = new Model(protocol, device);
        MainController mainController = createViewController(model);
        MainView mainView = mainController.getMainView();
        Scene scene = new Scene(mainView, 640, 480);
        stage.setTitle("Emuuuuuuuuuu");
        stage.setScene(scene);
        stage.show();

        Thread.ofVirtual().start(() -> {
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

        new RxTxController(mainView.getRxTxView(), model);

        return new MainController(mainView);
    }

}
