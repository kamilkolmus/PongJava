package com.pong.klient;

import javafx.scene.layout.Pane;

public class MultiPlayerMenuView extends Pane {

    static int WIDTH=800, HEIGHT = 400;

    MultiPlayerMenuView() {
        setPrefSize(WIDTH, HEIGHT);
        setStyle("-fx-background-image: url(/com/pong/klient/1vs1_logo.png)");

        MainMenuView.MenuRect localRect = new MainMenuView.MenuRect();
        MainMenuView.MenuText localText = new MainMenuView.MenuText("Same Computer");
        MainMenuView.MenuPane localPane = new MainMenuView.MenuPane(localRect,localText);

        MainMenuView.MenuRect multiRect = new MainMenuView.MenuRect();
        MainMenuView.MenuText multiText = new MainMenuView.MenuText("LAN");
        MainMenuView.MenuPane multiPane = new MainMenuView.MenuPane(multiRect,multiText);

        MainMenuView.MenuVbox menuVbox = new MainMenuView.MenuVbox(new MainMenuView.MenuPane(localRect,localText),new MainMenuView.MenuPane(multiRect,multiText));
        getChildren().add(menuVbox);
        menuVbox.setLayoutY(190);
        menuVbox.setLayoutX(60);

        MainMenuView.MenuPane buttonPane;
        buttonPane = CreditsView.createSmallButton();
        getChildren().addAll(localPane,multiPane,buttonPane);
    }


}
