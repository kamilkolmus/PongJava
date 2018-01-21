package com.pong.serwer;

import com.pong.gameengine.GameEngine;
import com.pong.gameengine.GameInterface;
import com.pong.gameengine.PLAYERS;
import com.pong.gameengine.PLAYER_MOVE;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

interface OnlineGameInterface{
    void frameUpdate(Player player1,Player player2,double pos_player1,double pos_player2,double pos_ball_x,double pos_ball_y,int score_player1,int score_player2);
}

public class Game implements GameInterface {


    private  SimpleIntegerProperty id;
    private  SimpleStringProperty login1;
    private  SimpleStringProperty login2;
    private  SimpleStringProperty score=new SimpleStringProperty("0:0");
    private  SimpleStringProperty status=new SimpleStringProperty("Running...");

    OnlineGameInterface onlineGameInterface;

    Player player1;
    Player player2;

    private GameEngine gameEngine;

    private int rectangleX =20, rectangleY =120;

    private int WIDTH=800, HEIGHT = 400;
    private final double ball_radius=8;

    double player1_position=0;
    double player2_position=0;

    int score_player1=0;
    int score_player2=0;

    Game(int id,Player player1, Player player2,OnlineGameInterface onlineGameInterface){
        this.id= new SimpleIntegerProperty(id);
        this.login1=new SimpleStringProperty(player1.getLogin());
        this.login2=new SimpleStringProperty(player2.getLogin());
        this.player1=player1;
        this.player2=player2;
        this.onlineGameInterface=onlineGameInterface;
        gameEngine = new GameEngine(PLAYERS.TWO_PLAYERS,this,WIDTH,HEIGHT, rectangleX, rectangleY,ball_radius);
        gameEngine.start();
    }

    public int getId() {
        return id.get();
    }

    public String getScore() {
        return score.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getLogin1() {
        return login1.get();
    }

    public String getLogin2() {
        return login2.get();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setLogin1(String login1) {
        this.login1.set(login1);
    }

    public void setLogin2(String login2) {
        this.login2.set(login2);
    }

    public void setScore(String score) {
        this.score.set(score);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }


    void moveDown(String login){

        if(login.equals(login1.get())){
            System.out.println("player1 up");
            gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_DOWN);
        }else{
            System.out.println("player2 up");
            gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_DOWN);
        }
    }
    void moveUp(String login){


        if(login.equals(login1.get())){
            System.out.println("player1 up");
            gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_UP);
        }else{
            System.out.println("player2 up");
            gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_UP);
        }
    }
    void moveStop(String login){

        if(login.equals(login1.get())){
            System.out.println("player1 stop");
            gameEngine.movePlayer_1(PLAYER_MOVE.MOVE_STOP);
        }else{
            System.out.println("player2 stop");
            gameEngine.movePlayer_2(PLAYER_MOVE.MOVE_STOP);
        }
    }

    @Override
    public void ballPosition(double x, double y) {

        onlineGameInterface.frameUpdate(player1,player2,player1_position,player2_position,x,y,score_player1,score_player2);
    }

    @Override
    public void positionPlayer1(double y) {
        player1_position=y;
    }

    @Override
    public void positionPlayer2(double y) {
        player2_position=y;

    }

    @Override
    public void gameScore(int player1Score, int player2Score) {
        score_player1=player1Score;
        score_player2=player2Score;
        setScore(player1Score+":"+player2Score);
    }

    public void destroyGame() {
        gameEngine.stop();
        gameEngine.destroy();
    }
}
