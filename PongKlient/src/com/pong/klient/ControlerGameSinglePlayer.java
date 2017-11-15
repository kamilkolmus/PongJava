package com.pong.klient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControlerGameSinglePlayer {

    @FXML
    private Button button_return;

    @FXML
    void OnButtonReturnClick(ActionEvent event) {
        Main.getInstance().setSceneGameModeSelect();
    }

}
