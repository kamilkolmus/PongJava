package com.pong.klient;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class CreditsView extends Pane {

    static int WIDTH = 800, HEIGHT = 400;
    String[] credTexts = new String[]{"Game Creators", "Lead programmer: Kamil Kolmus", "FrontEnd Programmer: Tomasz Kuźnar", "Thanks for your time in our game ;)"};
    int i = 0;
    CreditsText creditsText;
    static Timeline timer2;


    public class CreditsText extends StackPane {
        Timeline timer;
        short x = 400, y = 340;

        CreditsText(String credText) {
            Text text = new Text(credText);
            setLayoutX(400 - (text.getLayoutBounds().getWidth() / 2));
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Impact", FontWeight.SEMI_BOLD, 15));
            setVisible(false);
            getChildren().add(text);


            timer = new Timeline(new KeyFrame(Duration.millis(16), (ActionEvent event) -> {
                System.out.println("timer");
                if (getLayoutY() < 340) {
                    setVisible(true);
                }
                setLayoutY(y -= 1);
                if (getLayoutY() < -150) timer.stop();

            }));
            timer.setCycleCount(Timeline.INDEFINITE);
            timer.play();


        }

        void timerStop() {
            timer.stop();
        }
    }

    public static MainMenuView.MenuPane createSmallButton(){
        MainMenuView.MenuRect rect = new MainMenuView.MenuRect();
        MainMenuView.MenuText text = new MainMenuView.MenuText("return");
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setHeight(30);
        rect.setWidth(70);
        text.setFont(Font.font("Impact", FontWeight.SEMI_BOLD, 15));
        MainMenuView.MenuPane buttonPane = new MainMenuView.MenuPane(rect,text);
        buttonPane.setLayoutX(WIDTH / 2 - (rect.getWidth() / 2));
        buttonPane.setLayoutY(HEIGHT - 40);
        return buttonPane;
    }

    CreditsView() {
        setPrefSize(WIDTH, HEIGHT);
        setStyle("-fx-background-image: url(/com/pong/klient/main_menu_logo.png)");
        MainMenuView.MenuPane buttonPane;
        buttonPane = createSmallButton();
        getChildren().addAll(buttonPane);

        buttonPane.setOnMouseClicked(event -> {
            Main.getInstance().setSceneGameModeSelect();
            timer2.stop();
        });


        timer2 = new Timeline(new KeyFrame(Duration.millis(500), (ActionEvent event) -> {
            System.out.println("timer2");

            if (i > credTexts.length - 1) {
                if (creditsText.getLayoutY() < ((creditsText.getHeight() * i) - 150)) {
                    Main.getInstance().setSceneGameModeSelect();
                    timer2.stop();
                }
            } else {

                creditsText = new CreditsText(credTexts[i]);
                i++;
                getChildren().addAll(creditsText);
            }

        }));
        timer2.setCycleCount(Timeline.INDEFINITE);
        timer2.play();

    }


}
