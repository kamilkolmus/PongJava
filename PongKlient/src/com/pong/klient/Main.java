package com.pong.klient;


import com.pong.gameengine.PLAYERS;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


public class Main extends Application  {

    private static Main instance;
    Stage window;
    MainMenuView mainMenuView;
    Scene mainMenuScene;
    GameViewNetworkMultiplayer gameViewNetworkMultiplayer;


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Game Pong");
        //window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_select_game_mode.fxml"))));
        mainMenuView = new MainMenuView();
        mainMenuScene = new Scene(mainMenuView);
        window.setScene(mainMenuScene);
        window.show();
        window.setResizable(false);
        System.out.println("java version: "+System.getProperty("java.version"));
        System.out.println("javafx.version: " + System.getProperty("javafx.version"));

    }


    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }

    void setSceneSettings(){
        try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_settings.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setSceneSinglePlayer(){

        GameView gameView = new GameView(PLAYERS.VS_BOT);
        window.setScene(new Scene(gameView));
        gameView.addListener(window);

    }


    void setSceneGameModeSelect(){
        window.setScene(mainMenuScene);
        /*try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_select_game_mode.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

     void setSceneMultiPlayer() {


         try {
             window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scane_multiplayer_game_choice.fxml"))));
         } catch (IOException e) {
             e.printStackTrace();
         }

    }


    void setSceneGameMultiplayerLocal(){
        GameView gameView = new GameView(PLAYERS.TWO_PLAYERS);
        window.setScene(new Scene(gameView));
        gameView.addListener(window);

    }
    void setSceneGameMultiplayerNetwork(){
        try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scane_connection_to_serwer.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setSceneGameMultiplayerNetworkGame(){

        try {
            gameViewNetworkMultiplayer=new GameViewNetworkMultiplayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getInstance().window.setScene(new Scene(gameViewNetworkMultiplayer));
        gameViewNetworkMultiplayer.addListener(window);

    }


    public Main() {
        instance = this;
    }

    static Main getInstance() {
        return instance;
    }



}


