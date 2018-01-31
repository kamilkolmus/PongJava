package com.pong.klient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class ControlerMultiplayerGame {


    @FXML
    void onLocalMultiplayerClick(ActionEvent event) {

        Main.getInstance().setSceneGameMultiplayerLocal();


    }


    public void onNetworkMultiplayerClick(ActionEvent actionEvent) {

        Main.getInstance().setSceneGameMultiplayerNetwork();

    }
    public void onReturnClick(ActionEvent actionEvent) {

        Main.getInstance().setSceneGameModeSelect();

    }



}
