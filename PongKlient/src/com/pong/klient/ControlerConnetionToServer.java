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
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ControlerConnetionToServer {

    static ChannelHandlerContext channelHandlerContext;
    static String login = "login";
    static String address = "192.168.56.1";
    static int port = 8898;
    static boolean is_connected_to_server = false;

    static EventLoopGroup group;
    static int GameID=0;


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
    private Button button_connect_disconnect;

    @FXML
    public void onConnect_DisconnectClick(ActionEvent actionEvent) throws UnknownHostException {



        connect_disconect();



    }



    void connect_disconect() throws UnknownHostException {

        if(!is_connected_to_server){
            login = textfield_login.getText();
            address = textfield_address.getText();
            port = Integer.parseInt(textfield_port.getText());
            createTCPClient();
            Thread thread= new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!is_connected_to_server){
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                        loginToServer();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
        }else{
            loguotFromServer();
        }
    }


    @FXML
    void onGetPlayerListClick(ActionEvent actionEvent) throws UnknownHostException {
        if (is_connected_to_server) {
            getPlayerList();
        }
    }

    @FXML
    void onReturnClick(ActionEvent actionEvent) {
        Main.getInstance().setSceneGameModeSelect();
    }

    @FXML
    public void onInviteToGameClick(ActionEvent actionEvent) {
        try {
            inviteToGame();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {


        Label placeholder = new Label();
        placeholder.setText("NO LOGGED PLAYERS");
        table_players.setPlaceholder(placeholder);
        placeholder.setTextFill(Color.WHITE);

        tv_player_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tv_player_ip.setCellValueFactory(new PropertyValueFactory<>("Ip"));
        tv_player_login.setCellValueFactory(new PropertyValueFactory<>("login"));
        tv_player_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        table_players.setItems(players);


        textfield_address.setText(address);
        textfield_port.setText(Integer.toString(port));
        textfield_login.setText(login);
        label_server_status.setText("");
        if(is_connected_to_server){
            button_connect_disconnect.setText("Disconnect");
        }else{
            button_connect_disconnect.setText("Connect");
            players.clear();
        }


    }
    void inviteToGame() throws UnknownHostException {
        Player player = table_players.getSelectionModel().getSelectedItem();
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "INVITE" + "\t" + player.getLogin(), CharsetUtil.UTF_8));
    }


    void getPlayerList() throws UnknownHostException {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "GET_PLAYERS", CharsetUtil.UTF_8));

    }

    void loginToServer() throws UnknownHostException {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "LOGIN" + "\t" + Inet4Address.getLocalHost().getHostAddress(), CharsetUtil.UTF_8));

    }
    void loguotFromServer() throws UnknownHostException {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "LOGOUT" + "\t" + Inet4Address.getLocalHost().getHostAddress(), CharsetUtil.UTF_8));

    }

    void moveUp() throws UnknownHostException {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "UP"+"\t"+GameID, CharsetUtil.UTF_8));

    }

    void moveDown() throws UnknownHostException {

        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "DOWN"+"\t"+GameID, CharsetUtil.UTF_8));
    }

    void moveStop() throws UnknownHostException {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "STOP"+"\t"+GameID, CharsetUtil.UTF_8));

    }

    public void endGame() {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "END_GAME"+ "\t"+GameID, CharsetUtil.UTF_8));
    }




    public class ClientHandler extends SimpleChannelInboundHandler {

        @Override
        public void channelActive(ChannelHandlerContext channelHandlerContext) throws UnknownHostException {
            ControlerConnetionToServer.channelHandlerContext = channelHandlerContext;
            labelStatus("Connected to Server");
        }


        public void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) throws IOException {
            //  System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
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
            //  System.out.println("Client received: " + received);
            response(received);

        }

        void response(String response) throws IOException {
            List<String> list = new LinkedList<String>(Arrays.asList(response.trim().split("\t")));

//            if (list.size() < 1) {
//                System.out.println("undefinited response");
//            }
            switch (list.get(0)) {
                case "LOGIN":
                    System.out.println(Arrays.toString(list.toArray()));
                    if (list.get(1).equals("OK")) {

                        System.out.println("LOGIN:OK");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                button_connect_disconnect.setText("Disconnect");
                                label_server_status.setText("Logged to server");
                            }
                        });
                    } else {
                        Thread thread= new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        label_server_status.setText("Login ERROR. Try another login");
                                    }
                                });
                                try {
                                    group.shutdownGracefully().sync();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
//                                while(group.isTerminated()){ }
                            }
                        });
                        thread.start();
                        System.out.println("LOGIN:ERROR");
                    }
                    break;

                case "LOGOUT":
                    System.out.println(Arrays.toString(list.toArray()));
                    if (list.get(1).equals("OK")) {

                        Thread thread= new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        button_connect_disconnect.setText("Connect");
                                        players.clear();
                                        label_server_status.setText("Logout OK");
                                    }
                                });
                                try {
                                    group.shutdownGracefully().sync();
                                    is_connected_to_server=false;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
//                                while(group.isTerminated()){ }
                            }
                        });
                        thread.start();

                        System.out.println("LOGOUT:OK");
                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                label_server_status.setText("Logout Error");
                            }
                        });
                        System.out.println("LOGOUT:ERROR");
                    }
                    break;
                case "GET_PLAYERS":
                    System.out.println(Arrays.toString(list.toArray()));
                    System.out.println("GET_PLAYERS COMMAND RECEIVED");
                    players.clear();
                    int k = 0;
                    for (int i = 1; i < list.size(); i += 3) {
                        if(!list.get(i).equals(login)){
                            k++;
                            players.add(new Player(k, list.get(i), list.get(i + 1),list.get(i + 2)));
                        }

                    }

                    break;
                case "INVITE":
                    System.out.println(Arrays.toString(list.toArray()));
                    AllertWindow(list.get(1));

                    break;
                case "INVITE_RESPONSE":
                    System.out.println(Arrays.toString(list.toArray()));
                    labelStatus("INVITATION from: " + list.get(1) + " " + list.get(2));
                    if(list.get(2).equals("OK")){
                        labelStatus("INVITATION from: " + list.get(1) + " " + list.get(2) + " " +list.get(3));
                        GameID=Integer.parseInt(list.get(3));
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                System.out.println(""+GameID);
                                Main.getInstance().setSceneGameMultiplayerNetworkGame();
                            }
                        });

                    }

                    break;
                case "GAME_FRAME":
                    if(Main.getInstance().gameViewNetworkMultiplayer!=null){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Main.getInstance().gameViewNetworkMultiplayer.updateFrame(Double.parseDouble( list.get(1)),
                                        Double.parseDouble(list.get(2)),
                                        Double.parseDouble( list.get(3)),
                                        Double.parseDouble( list.get(4)),
                                        Integer.parseInt(list.get(5)),
                                        Integer.parseInt(list.get(6)));
                            }
                        });

                    }

                    break;

                case "END_GAME":
                    //    loguotFromServer();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Main.getInstance().setSceneGameMultiplayerNetwork();
                        }
                    });

                    break;

                default:
                    System.out.println("undefinited command");
                    break;
            }
        }
    }

    public static void AllertWindow(String login) {


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("INVITATION FOR GAME");
                alert.setHeaderText("INVITATION FOR GAME FROM: " + login);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "INVITE_RESPONSE" + "\t" + login + "\t" + "OK", CharsetUtil.UTF_8));
                } else {
                    channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("\t" +login + "\t" + "INVITE_RESPONSE" + "\t" + login + "\t" + "CANCEL", CharsetUtil.UTF_8));
                }
            }
        });
    }

    public  void labelStatus(String msg) {


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label_server_status.setText(msg);
            }
        });
    }


    void createTCPClient() {

        group = new NioEventLoopGroup();
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
                    is_connected_to_server = true;
                    channelFuture.channel().closeFuture().sync();

                } catch (Exception e) {

                    e.printStackTrace();
                    System.out.println("Klient exception");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            button_connect_disconnect.setText("Connect");
                        }
                    });
                } finally {

                    try {
                        group.shutdownGracefully().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            button_connect_disconnect.setText("Connect");
                        }
                    });
                    System.out.println("Klient disconected");
                    labelStatus("Server Disconnected");
                    players.clear();
                    is_connected_to_server = false;
                }
            }
        };
        thread.start();

    }


}
