package com.pong.serwer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Controler {



    @FXML
    private TableView<Player> tableview_players;


    @FXML
    private TableColumn<Player, Integer> tv_player_id;

    @FXML
    private TableColumn<Player, String> tv_player_login;

    @FXML
    private TableColumn<Player, String> tv_player_ip;

    @FXML
    private TableColumn<Game, String> tv_player_status;



    @FXML
    private TableView<Game> tableview_games;

    @FXML
    private TableColumn<Game, Integer> tv_game_id;

    @FXML
    private TableColumn<Game, String> tv_game_player1;

    @FXML
    private TableColumn<Game, String> tv_game_player2;

    @FXML
    private TableColumn<Game, String> tv_game_score;

    @FXML
    private TableColumn<Game, String> tv_game_status;


    @FXML
    private ToggleButton button_serv_stat;

    @FXML
    private TextField text_filed_port;

    @FXML
    private Label label_ip;

    @FXML
    void onButtonSerwerStatusClick(ActionEvent event) {
        if(button_serv_stat.isSelected()){

           System.out.println("togle button works");
            button_serv_stat.setText("ON");
            Main.getInstance().startServer();
        }else{
            button_serv_stat.setText("OFF");
            Main.getInstance().stopServer();
        }

    }
    @FXML
    public void initialize() {
        button_serv_stat.setText("OFF");
        Label placeholder = new Label();
        placeholder.setText("NO LOGGED PLAYERS");
        tableview_players.setPlaceholder(placeholder);

        Label placeholder2 = new Label();
        placeholder.setText("NO GAMES");
        tableview_games.setPlaceholder(placeholder2);


        try {
            label_ip.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        text_filed_port.setText(""+Main.port);

        tv_game_id.setCellValueFactory( new  PropertyValueFactory<>("Id"));
        tv_game_player1.setCellValueFactory( new  PropertyValueFactory<>("login1"));
        tv_game_player2.setCellValueFactory( new  PropertyValueFactory<>("login2"));
        tv_game_score.setCellValueFactory( new  PropertyValueFactory<>("score"));
        tv_game_status.setCellValueFactory( new  PropertyValueFactory<>("status"));


        tv_player_id.setCellValueFactory( new  PropertyValueFactory<>("Id"));
        tv_player_ip.setCellValueFactory( new  PropertyValueFactory<>("Ip"));
        tv_player_login.setCellValueFactory( new  PropertyValueFactory<>("login"));
        tv_player_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableview_players.setItems(Main.getInstance().players);
        tableview_games.setItems(Main.getInstance().games);

    }



    public Integer getPort() {
        return Integer.parseInt(text_filed_port.getText());
    }
}