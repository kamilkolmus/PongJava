package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControlerSettings {

    @FXML
    private Button button_return;

    @FXML
    void OnReturnButtonCilck(ActionEvent event) {
        Main.getInstance().setSceneGameModeSelect();
    }

}
