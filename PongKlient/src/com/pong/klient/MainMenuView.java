package com.pong.klient;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;


public class MainMenuView extends Pane {

    public static class MenuText extends Text{
        MenuText(String text){
            setText(text);
            setFont(Font.font("Impact", FontWeight.SEMI_BOLD, 18));
            setFill(Color.WHITE);
        }

    }


    public static class MenuRect extends Rectangle{
        MenuRect(){
           setFill(Color.GRAY);
           setOpacity(0.4);
           setWidth(300);
           setHeight(50);

        }

    }


    public static class MenuPane extends StackPane{

        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[] {
                new Stop(0, Color.BLACK),
                new Stop(0.2, Color.GRAY),
                new Stop(0.8, Color.GRAY),
                new Stop(1, Color.BLACK)
        });

        MenuPane(MenuRect rect, MenuText text){

//           Media sound = new Media(new File("com/pong/klient/click.wav").toURI().toString());
//           MediaPlayer mediaPlayer = new MediaPlayer(sound);


            this.setOnMouseEntered(event -> {
                rect.setFill(gradient);
          //      mediaPlayer.play();
            });
            this.setOnMouseExited(event -> {
                rect.setFill(Color.GRAY);
         //       mediaPlayer.stop();
            });
            getChildren().addAll(rect,text);

            this.setOnMouseClicked(event -> {
                if(text.getText() == "Single Player") {
                    Main.getInstance().setSceneSinglePlayer();
                }else if(text.getText() == "Multi Player"){
                    Main.getInstance().setSceneMultiPlayer();
                }
                else if(text.getText() == "Credits"){
                    Main.getInstance().setSceneCreditsView();
                }
            });

        }
    }
    public static class MenuVbox extends VBox{
        MenuVbox(MenuPane... items){
            setLayoutX(150);
            setLayoutY(150);

            getChildren().add(createSeparator());

            for (MenuPane item : items) {
                getChildren().addAll(item, createSeparator());
            }
        }
        private Line createSeparator() {
            Line sep = new Line();
            sep.setEndX(300);
            sep.setStroke(Color.DARKGREY);
            sep.setStyle("-fx-background-size: 99");
            return sep;
        }

    }

    private int WIDTH=800, HEIGHT = 400;
    MenuRect singlePlayerRect, multiPlayerRect,settingsRect;
    MenuText textSingle,textMulti,textSettings;
    StackPane paneSingle,paneMulti,paneSettings;
    VBox vBox = new VBox();

    MainMenuView(){
        setPrefSize(WIDTH,HEIGHT);
        setStyle("-fx-background-image: url(/com/pong/klient/main_menu_logo2.png)");
        singlePlayerRect = new MenuRect();
        textSingle = new MenuText("Single Player");
        paneSingle = new MenuPane(singlePlayerRect,textSingle);

        multiPlayerRect = new MenuRect();
        textMulti = new MenuText("Multi Player");
        paneMulti = new MenuPane(multiPlayerRect,textMulti);

        settingsRect = new MenuRect();
        textSettings = new MenuText("Credits");
        paneSettings = new MenuPane(settingsRect,textSettings);

        MenuVbox menuVbox = new MenuVbox(new MenuPane(singlePlayerRect,textSingle),new MenuPane(multiPlayerRect,textMulti), new MenuPane(settingsRect,textSettings));
        getChildren().add(menuVbox);
        menuVbox.setLayoutY(190);
        menuVbox.setLayoutX(60);
        getChildren().addAll(paneMulti,paneSettings,paneSingle);

    }

}
