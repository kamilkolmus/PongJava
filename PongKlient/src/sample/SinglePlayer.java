package sample;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.Graphics;


public class SinglePlayer extends Pane {


    Rectangle player,bot;
    Circle ball;
    Line line;
    AnimationTimer timer,playerMoveTimer;
    Button returnButton;
    Label labelPlayerScore,labelBotScore;
    int scorePlayer=0,scoreBot=0;

    int WIDTH=1000, HEIGHT = 400;
    int speedX=3, speedY=3 , dx =speedX , dy= speedY;

    final double playerSpeed = 200 ; //prędkość poruszania się gracza.
    final double minY = 0 ; //Minimalna pozycja gracza
    final double maxY = HEIGHT - 80 ; //Maksymalna pozycja gracza
    final DoubleProperty rectangleVelocity = new SimpleDoubleProperty();
    final LongProperty lastUpdateTime = new SimpleLongProperty();





    SinglePlayer(){

        setPrefSize(WIDTH,HEIGHT);
        setStyle("-fx-background-color: black");
        line = new Line(WIDTH/2,0,WIDTH/2,HEIGHT);
        line.setStroke(Color.WHITE);

        returnButton = new Button("return");
        returnButton.setLayoutX(WIDTH/2-25);
        returnButton.setLayoutY(HEIGHT-25);


        labelPlayerScore= new Label("0");
        labelPlayerScore.setPrefWidth(Region.USE_COMPUTED_SIZE);
        labelPlayerScore.setTextFill(Color.WHITE);
        labelPlayerScore.setFont(Font.font("Cambria", 25));
        labelBotScore= new Label("0");
        labelBotScore.setPrefWidth(Region.USE_COMPUTED_SIZE);
        labelBotScore.setTextFill(Color.WHITE);
        labelBotScore.setFont(Font.font("Cambria", 25));


        labelBotScore.setLayoutX(WIDTH/2+50);
        labelPlayerScore.setLayoutX(WIDTH/2-(50+labelPlayerScore.getWidth()));

        player = new Rectangle(10,80,Color.WHITE);
        player.setLayoutX(0);
        player.setLayoutY(HEIGHT/2-40);

        bot = new Rectangle(10,80,Color.WHITE);
        bot.setLayoutX(WIDTH-10);
        bot.setLayoutY(HEIGHT/2-40);

        ball= new Circle(5);
        ball.setStroke(Color.WHITE);
        ball.setFill(Color.WHITE);
        ball.setLayoutX(WIDTH/2);
        ball.setLayoutY(HEIGHT/2);

        getChildren().addAll(line,bot,player,ball,returnButton,labelPlayerScore,labelBotScore);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameUpdate();
            }
        };

        timer.start();

        new AnimationTimer(){
            @Override
            public void handle(long now){
                if (lastUpdateTime.get() > 0) {
                    final double elapsedSeconds = (now - lastUpdateTime.get()) / 1_000_000_000.0 ;
                    final double deltaY = elapsedSeconds * rectangleVelocity.get();

                    final double oldY = player.getLayoutY();
                    final double newY = Math.max(minY, Math.min(maxY, oldY + deltaY));
                    player.setLayoutY(newY);
                }
                lastUpdateTime.set(now);
            }
        }.start();

    }

    void gameUpdate() {
        double x = ball.getLayoutX(), y = ball.getLayoutY();

        //System.out.print("x="+x+" y="+y+"\n\r");

        if(x>=WIDTH-12.5 && y>bot.getLayoutY() && y < bot.getLayoutY() +80) {
            dx = -speedX;
            speedX++;
        }
        if(x<= 12.5 && y>player.getLayoutY()&&y<player.getLayoutY()+80){

            dx = speedX;
            speedX++;

        }
        if(x<-5){

            scoreBot++;
            labelBotScore.setText(""+scoreBot);
            ball.setLayoutX(WIDTH/2);
            ball.setLayoutY(HEIGHT/2);
            speedX=3;
            dx=speedX;
        }
        if(x>WIDTH+100){

            scorePlayer++;
            labelPlayerScore.setText(""+scorePlayer);
            ball.setLayoutX(WIDTH/2);
            ball.setLayoutY(HEIGHT/2);
            speedX=3;
            dx=speedX;
        }


        if(y<=0) dy= speedY;
        if(y>=HEIGHT-5) dy = - speedY;


        //update ball
        ball.setLayoutX(ball.getLayoutX()+ dx);
        ball.setLayoutY(ball.getLayoutY()+dy);

        //botapdate
        if (x>WIDTH/2 && dx>0 && bot.getLayoutY()+20>y)     bot.setLayoutY(bot.getLayoutY()-5);
        if (x>WIDTH/2 && dx>0 && bot.getLayoutY()+60<y)     bot.setLayoutY(bot.getLayoutY()+5);
    }

    void goUp (){

        player.setLayoutY(player.getLayoutY()-15);

    }
    void goDown (){

        player.setLayoutY(player.getLayoutY()+15);

    }
    public void addListener(Stage window) {

        //Stara obsługa poruszania protokątem gracza
        /*window.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode()== KeyCode.A) goUp();
                if(event.getCode()== KeyCode.Z) goDown();
                if(event.getCode()== KeyCode.UP) goUp();
                if(event.getCode()== KeyCode.DOWN) goDown();

                System.out.print("setOnKeyPressed\n\r");
            }
        });*/

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.getInstance().setSceneGameModeSelect();
            }
        });

        window.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.Z) {
                    rectangleVelocity.set(playerSpeed);
                }if (event.getCode() == KeyCode.A) {
                    rectangleVelocity.set(-playerSpeed);
                }
            }
        });

        window.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.Z) {
                    rectangleVelocity.set(0);
                }
            }
        });
    }

}
