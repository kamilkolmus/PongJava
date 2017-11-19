package com.pong.serwer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controler {

    ObservableList<Player> players = FXCollections.observableArrayList();
    ObservableList<Game> games = FXCollections.observableArrayList();

    @FXML
    private TableView<Player> tableview_players;


    @FXML
    private TableColumn<Player, Integer> tv_player_id;

    @FXML
    private TableColumn<Player, String> tv_player_login;

    @FXML
    private TableColumn<Player, String> tv_player_ip;

    @FXML
    private TableColumn<Player, String> tv_player_status;


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

    }
    @FXML
    public void initialize() {
        Label placeholder = new Label();
        placeholder.setText("NO LOGGED PLAYERS");
        tableview_players.setPlaceholder(placeholder);

        Label placeholder2 = new Label();
        placeholder.setText("NO GAMES");
        tableview_games.setPlaceholder(placeholder2);

        players.add(new Player(1,"sdsd","100.100.100"));
        players.add(new Player(2,"cvdfv","10f.dfd.sds"));

        games.add(new Game(1,"pla","dsd"));
        games.add(new Game(2,"sdsd","fvfvf"));

        tv_game_id.setCellValueFactory( new  PropertyValueFactory<>("Id"));
        tv_game_player1.setCellValueFactory( new  PropertyValueFactory<>("player1"));
        tv_game_player2.setCellValueFactory( new  PropertyValueFactory<>("player2"));
        tv_game_score.setCellValueFactory( new  PropertyValueFactory<>("score"));
        tv_game_status.setCellValueFactory( new  PropertyValueFactory<>("status"));


        tv_player_id.setCellValueFactory( new  PropertyValueFactory<>("Id"));
        tv_player_ip.setCellValueFactory( new  PropertyValueFactory<>("Ip"));
        tv_player_login.setCellValueFactory( new  PropertyValueFactory<>("login"));
        tv_player_status.setCellValueFactory( new  PropertyValueFactory<>("status"));

        tableview_players.setItems(players);
        tableview_games.setItems(games);


    }

}