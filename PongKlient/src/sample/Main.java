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
        window.setTitle("Hello World");
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_select_game_mode.fxml"))));
        window.show();
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

        GameView singlePlayer = new GameView();
        window.setScene(new Scene(singlePlayer));
        singlePlayer.addListener(window);

    }

    public  void setSceneGameModeSelect(){
        try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_select_game_mode.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSceneMultiPlayer() {

    }
    public Main() {
        instance = this;
    }
    // static method to get instance of view
    public static Main getInstance() {
        return instance;
    }
}



//      ControlerGameMode sceneGameSinglePlayerControler;
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.load(getClass().getResource("scene_select_game_mode.fxml").openStream());
//        sceneGameSinglePlayerControler = (ControlerGameMode) fxmlLoader.getController();
//        sceneGameSinglePlayerControler.setInterface(this);