package com.pong.klient;

import com.pong.gameengine.GameEngine;
import com.pong.gameengine.GameInterface;
import com.pong.gameengine.PLAYERS;
import com.pong.gameengine.PLAYER_MOVE;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.UnknownHostException;


public class GameView extends Pane implements GameInterface {


    private final double ball_radius=8;
    private Rectangle player_1, player_2;
    private Circle ball;
    private Line line;
    private Button returnButton;
    private Label labelPlayerScore,labelBotScore;

    private GameEngine gameEngine;

    private int rectangleX =20, rectangleY =120;

    private int WIDTH=800, HEIGHT = 400;

    com.pong.gameengine.PLAYERS players;


    GameView(PLAYERS players){
        this.players=players;

        gameEngine = new GameEngine(players,this,WIDTH,HEIGHT, rectangleX, rectangleY,ball_radius);
        setPrefSize(WIDTH,HEIGHT);
        setStyle("-fx-background-color: black");
        line = new Line(WIDTH/2,0,WIDTH/2,HEIGHT);
        line.setStroke(Color.WHITE);

        returnButton = new Button("return");
        returnButton.setStyle("-fx-background-color: black");
        returnButton.setTextFill(Paint.valueOf("#ffffff"));
        returnButton.setLayoutX(WIDTH/2-25);
        returnButton.setLayoutY(HEIGHT-25);


        labelPlayerScore= new Label("0");
        labelPlayerScore.setMinWidth(100);
        labelPlayerScore.setMaxWidth(100);
        labelPlayerScore.setPrefSize(100,30);
        labelPlayerScore.setAlignment(Pos.CENTER);
        labelPlayerScore.setStyle("-fx-background-color: transparent");
        labelPlayerScore.setFont(Font.font ("Verdana", 20));
        labelPlayerScore.setTextFill(Paint.valueOf("#ffffff"));


        labelBotScore= new Label("0");
        labelBotScore.setMinWidth(100);
        labelBotScore.setMaxWidth(100);
        labelBotScore.setPrefSize(100,30);
        labelBotScore.setAlignment(Pos.CENTER);
        labelBotScore.setStyle("-fx-background-color: transparent");
        labelBotScore.setFont(Font.font ("Verdana", 20));
        labelBotScore.setTextFill(Paint.valueOf("#ffffff"));


        getChildren().addAll(labelBotScore,labelPlayerScore);

        labelPlayerScore.setLayoutX(WIDTH/2-10-labelPlayerScore.prefWidth(-1));

        labelBotScore.setLayoutX(WIDTH/2+10);


        player_1 = new Rectangle(rectangleX, rectangleY,Color.WHITE);
        player_1.setLayoutX(0);
        player_1.setLayoutY(HEIGHT/2-rectangleY/2);

        player_2 = new Rectangle(rectangleX,rectangleY,Color.WHITE);
        player_2.setLayoutX(WIDTH-rectangleX);
        player_2.setLayoutY(HEIGHT/2-rectangleY/2);

        ball= new Circle(ball_radius);
        ball.setStroke(Color.WHITE);
        ball.setFill(Color.WHITE);
        ball.setLayoutX(WIDTH/2);
        ball.setLayoutY(HEIGHT/2);

        getChildren().addAll(line, player_2, player_1,ball,returnButton);

        gameEngine.start();

    }
    KeyCode player1kode;
    KeyCode player2kode;

    public void addListener(Stage window) {

        window.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(players==PLAYERS.VS_BOT){
                    if(event.getCode()== KeyCode.A) {gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_UP);player1kode=event.getCode();}
                    if(event.getCode()== KeyCode.Z) {gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_DOWN);player1kode=event.getCode();}

                    if (event.getCode() == KeyCode.K) {gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_UP);player2kode=event.getCode();}
                    if (event.getCode() == KeyCode.M) {gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_DOWN);player2kode=event.getCode();}


                }else{
                    if(event.getCode()== KeyCode.A) {gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_UP);player1kode=event.getCode();
                        try {
                            Main.getInstance().moveUp();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                    if(event.getCode()== KeyCode.Z) {gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_DOWN);player1kode=event.getCode();
                        try {
                            Main.getInstance().moveDown();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }

                    if (event.getCode() == KeyCode.K) {gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_UP);player2kode=event.getCode();
                        try {
                            Main.getInstance().moveDown();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                    if (event.getCode() == KeyCode.M) {gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_DOWN);player2kode=event.getCode();
                        try {
                            Main.getInstance().moveUp();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                }


                System.out.print("setOnKeyPressed\n\r"+event.getCode());
            }
        });
        window.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(players==PLAYERS.VS_BOT){
                    if(event.getCode()== player1kode) gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_STOP);
                    if(event.getCode() == player2kode) gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_STOP);

                }else{
                    if(event.getCode()== player1kode) {gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_STOP);
                        try {
                            Main.getInstance().moveStop();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                    if(event.getCode() == player2kode) gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_STOP);

                }


            }
        });
        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameEngine.stop();
                gameEngine.destroy();
                Main.getInstance().setSceneGameModeSelect();
            }
        });
    }

    @Override
    public void ballPosition(double x, double y) {
        ball.setLayoutX(x);
        ball.setLayoutY(y);
    }

    @Override
    public void positionPlayer1(double y) {
        player_1.setLayoutY(y);
    }

    @Override
    public void positionPlayer2(double y) {
        player_2.setLayoutY(y);
    }

    @Override
    public void gameScore(int player1Score, int player2Score) {
        labelPlayerScore.setText(""+player1Score);
        labelBotScore.setText(""+player2Score);

    }
}
