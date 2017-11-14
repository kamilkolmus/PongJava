package sample;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;



public class GameView extends Pane implements GameInterface {


    Rectangle player,bot;
    Circle ball;
    Line line;
    AnimationTimer timer;
    Button returnButton;
    Label labelPlayerScore,labelBotScore;

    GameEngine gameEngine;

    int rectangleX =10, rectangleY =80;


    int WIDTH=600, HEIGHT = 400;


    GameView(){

        gameEngine = new GameEngine(PLAYERS.VS_BOT,this,WIDTH,HEIGHT, rectangleX, rectangleY);
        setPrefSize(WIDTH,HEIGHT);
        setStyle("-fx-background-color: black");
        line = new Line(WIDTH/2,0,WIDTH/2,HEIGHT);
        line.setStroke(Color.WHITE);

        returnButton = new Button("return");
        returnButton.setLayoutX(WIDTH/2-25);
        returnButton.setLayoutY(HEIGHT-25);


        labelPlayerScore= new Label("0");
        labelBotScore= new Label("0");

        labelBotScore.setLayoutX(WIDTH/2+50);
        labelPlayerScore.setLayoutX(WIDTH/2-50);

        player = new Rectangle(rectangleX, rectangleY,Color.WHITE);
        player.setLayoutX(0);
        player.setLayoutY(HEIGHT/2-rectangleY/2);

        bot = new Rectangle(rectangleX,rectangleY,Color.WHITE);
        bot.setLayoutX(WIDTH-10);
        bot.setLayoutY(HEIGHT/2-rectangleY/2);

        ball= new Circle(5);
        ball.setStroke(Color.WHITE);
        ball.setFill(Color.WHITE);
        ball.setLayoutX(WIDTH/2);
        ball.setLayoutY(HEIGHT/2);

        getChildren().addAll(line,bot,player,ball,returnButton,labelPlayerScore,labelBotScore);

        gameEngine.start();

    }

    public void addListener(Stage window) {
        window.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode()== KeyCode.A) gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_UP);
                if(event.getCode()== KeyCode.Z) gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_DOWN);
                if(event.getCode()== KeyCode.UP) gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_UP);
                if(event.getCode()== KeyCode.DOWN) gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_DOWN);

                System.out.print("setOnKeyPressed\n\r");
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
    public void ballPosition(int x, int y) {
        ball.setLayoutX(x);
        ball.setLayoutY(y);
    }

    @Override
    public void positionPlayer1(int y) {
        player.setLayoutY(y);
    }

    @Override
    public void positionPlayer2(int y) {
        bot.setLayoutY(y);
    }

    @Override
    public void gameScore(int player1Score, int player2Score) {
        labelPlayerScore.setText(""+player1Score);
        labelBotScore.setText(""+player2Score);

    }
}
