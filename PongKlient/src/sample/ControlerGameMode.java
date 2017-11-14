package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class ControlerGameMode {



    @FXML
    private Button button_settings;

    @FXML
    private AnchorPane content;

    @FXML
    void OnSettingsButtonClick(ActionEvent event) {

        Main.getInstance().setSceneSettings();
    }


    public void OnSinglePlayerButton(ActionEvent actionEvent) {

        Main.getInstance().setSceneSinglePlayer();

    }

    public void OnMutliPlayerButtonClick(ActionEvent actionEvent) {

        Main.getInstance().setSceneMultiPlayer();

    }
}
