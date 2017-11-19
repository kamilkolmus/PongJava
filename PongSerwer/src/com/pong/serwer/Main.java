package com.pong.serwer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {

    private static Main instance;
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window= primaryStage;
        window.setTitle("Game Server");
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("root_view.fxml"))));
        window.show();
        window.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
