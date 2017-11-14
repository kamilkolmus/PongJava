package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application  {

    private static Main instance;
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window= primaryStage;
        window.setTitle("Game Pong");
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_select_game_mode.fxml"))));
        window.show();
        window.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }

    public  void setSceneSettings(){
        try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_settings.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void setSceneSinglePlayer(){

        GameView gameView = new GameView(PLAYERS.VS_BOT);
        window.setScene(new Scene(gameView));
        gameView.addListener(window);

    }

    public  void setSceneGameModeSelect(){
        try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_select_game_mode.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSceneMultiPlayer() {

        GameView gameView = new GameView(PLAYERS.TWO_PLAYERS);
        window.setScene(new Scene(gameView));
        gameView.addListener(window);

    }
    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }
}


