package com.pong.klient;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ControlerConnetionToServer {

    static ChannelHandlerContext channelHandlerContext;
    static String login = "my_Login1";
    static String address = "localhost";
    static int port = 8898;
    static boolean TCPConnetion = false;


    public static ObservableList<Player> players = FXCollections.observableArrayList();

    @FXML
    private TableView<Player> table_players;

    @FXML
    private TableColumn<Player, Integer> tv_player_id;

    @FXML
    private TableColumn<Player, String> tv_player_login;

    @FXML
    private TableColumn<Player, String> tv_player_ip;

    @FXML
    private TableColumn<Player, String> tv_player_status;

    @FXML
    private TextField textfield_address;

    @FXML
    private TextField textfield_login;

    @FXML
    private TextField textfield_port;

    @FXML
    private  Label label_server_status;

    @FXML
    void onConnectClick(ActionEvent event) {

        login = textfield_login.getText();
        address = textfield_address.getText();
        port = Integer.parseInt(textfield_port.getText());
        createTCPClient();


    }

    @FXML
    void onGetPlayerListClick(ActionEvent actionEvent) throws UnknownHostException {
        if (TCPConnetion) {
            getPlayerList();
        }
    }

    @FXML
    void onReturnClick(ActionEvent actionEvent) {
        Main.getInstance().setSceneGameModeSelect();
    }

    void createTCPClient() {

        EventLoopGroup group = new NioEventLoopGroup();
        Thread thread = new Thread() {
            public void run() {

                try {
                    Bootstrap clientBootstrap = new Bootstrap();

                    clientBootstrap.group(group);
                    clientBootstrap.channel(NioSocketChannel.class);
                    clientBootstrap.remoteAddress(new InetSocketAddress(address, port));
                    clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
                    ChannelFuture channelFuture = clientBootstrap.connect().sync();
                    TCPConnetion = true;
                    channelFuture.channel().closeFuture().sync();

                } catch (Exception e) {

                    e.printStackTrace();
                    System.out.println("Klient exception");

                } finally {

                    try {
                        group.shutdownGracefully().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Klient disconected");
                    labelStatus("Server Disconnected");
                    TCPConnetion = false;
                }
            }
        };
        thread.start();

    }

    @FXML
    public void onLoginClick(ActionEvent actionEvent) {
        try {
            logToServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onInviteToGameClick(ActionEvent actionEvent) {
        Player player = table_players.getSelectionModel().getSelectedItem();
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(login + "\t" + "INVITE" + "\t" + player.getLogin(), CharsetUtil.UTF_8));
    }

    @FXML
    public void initialize() {


        Label placeholder = new Label();
        placeholder.setText("NO LOGGED PLAYERS");
        table_players.setPlaceholder(placeholder);

        tv_player_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tv_player_ip.setCellValueFactory(new PropertyValueFactory<>("Ip"));
        tv_player_login.setCellValueFactory(new PropertyValueFactory<>("login"));
        tv_player_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        table_players.setItems(players);

        textfield_address.setText(address);
        textfield_port.setText(Integer.toString(port));
        textfield_login.setText(login);
        label_server_status.setText("trololo");


    }


    void getPlayerList() throws UnknownHostException {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(login + "\t" + "GET_PLAYERS", CharsetUtil.UTF_8));

    }

    void logToServer() throws UnknownHostException {
        ControlerConnetionToServer.channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(login + "\t" + "LOGIN" + "\t" + Inet4Address.getLocalHost().getHostAddress(), CharsetUtil.UTF_8));

    }

    void moveUp() throws UnknownHostException {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(login + "\t" + "UP", CharsetUtil.UTF_8));

    }

    void moveDown() throws UnknownHostException {

        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(login + "\t" + "DOWN", CharsetUtil.UTF_8));
    }

    void moveStop() throws UnknownHostException {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(login + "\t" + "STOP", CharsetUtil.UTF_8));

    }





    public class ClientHandler extends SimpleChannelInboundHandler {

        @Override
        public void channelActive(ChannelHandlerContext channelHandlerContext) throws UnknownHostException {
            ControlerConnetionToServer.channelHandlerContext = channelHandlerContext;
            labelStatus("Connected to Server");
        }


        public void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) {
            System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
            response(in.toString(CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
            cause.printStackTrace();
            channelHandlerContext.close();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
            ByteBuf inBuffer = (ByteBuf) o;
            String received = inBuffer.toString(CharsetUtil.UTF_8);
            System.out.println("Client received: " + received);
            response(received);

        }

        void response(String response) {
            List<String> list = new LinkedList<String>(Arrays.asList(response.trim().split("\t")));
            System.out.println(Arrays.toString(list.toArray()));
            if (list.size() < 1) {
                System.out.println("undefinited response");
            }
            switch (list.get(0)) {
                case "LOGIN":
                    if (list.get(1).equals("OK")) {

                        System.out.println("LOGIN:OK");
                    } else {
                        System.out.println("LOGIN:ERROR");
                    }
                    break;
                case "GET_PLAYERS":
                    System.out.println("GET_PLAYERS COMMAND RECEIVED");
                    players.clear();
                    int k = 0;
                    for (int i = 1; i < list.size(); i += 2) {
                        if(!list.get(i).equals(login)){
                            k++;
                            players.add(new Player(k, list.get(i), list.get(i + 1)));
                        }

                    }

                    break;
                case "INVITE":
                    AllertWindow(list.get(1));

                    break;
                case "INVITE_RESPONSE":
                    labelStatus("INVITATION from" + list.get(1) + " " + list.get(2));
                    break;
                case "UP":
                    break;
                case "DOWN":
                    break;
                case "STOP":
                    break;

                default:
                    System.out.println("undefinited command");
                    break;
            }
        }
    }

    public static void AllertWindow(String login) {

//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("INVITATION FOR GAME");
                alert.setHeaderText("INVITATION FOR GAME FROM: " + login);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(login + "\t" + "INVITE_RESPONSE" + "\t" + login + "\t" + "OK", CharsetUtil.UTF_8));
                } else {
                    channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(login + "\t" + "INVITE_RESPONSE" + "\t" + login + "\t" + "CANCEL", CharsetUtil.UTF_8));
                }
            }
        });
    }

    public  void labelStatus(String msg) {

//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label_server_status.setText(msg);
            }
        });
    }


}
