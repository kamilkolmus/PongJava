package com.pong.klient;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Player {


    private  SimpleStringProperty login;
    private  SimpleStringProperty Ip;
    private  SimpleIntegerProperty Id;
    private  SimpleStringProperty status;

    Player(int Id, String login, String Ip,String status){
        this.Id=new SimpleIntegerProperty(Id);
        this.login=new SimpleStringProperty(login);
        this.Ip=new SimpleStringProperty(Ip);
        this.status=new SimpleStringProperty(status);
    }

    public String getIp() {
        return Ip.get();
    }

    public String getLogin() {
        return login.get();
    }

    public int getId() {
        return Id.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setIp(String ip) {
        this.Ip.set(ip);
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public void setId(int id) {
        this.Id.set(id);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
