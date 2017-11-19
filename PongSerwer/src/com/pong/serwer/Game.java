package com.pong.serwer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Game {


    private  SimpleIntegerProperty id;
    private  SimpleStringProperty player1;
    private  SimpleStringProperty player2;
    private  SimpleStringProperty score=new SimpleStringProperty("0:0");
    private  SimpleStringProperty status=new SimpleStringProperty("status");

    Game(int id,String player1, String player2){
        this.id= new SimpleIntegerProperty(id);
        this.player1=new SimpleStringProperty(player1);
        this.player2=new SimpleStringProperty(player2);;
    }

    public int getId() {
        return id.get();
    }

    public String getPlayer1() {
        return player1.get();
    }

    public String getPlayer2() {
        return player2.get();
    }

    public String getScore() {
        return score.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setPlayer1(String player1) {
        this.player1.set(player1);
    }

    public void setPlayer2(String player2) {
        this.player2.set(player2);
    }

    public void setScore(String score) {
        this.score.set(score);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
