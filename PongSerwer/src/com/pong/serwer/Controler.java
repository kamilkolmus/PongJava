package com.pong.serwer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Controler {


    static int port = 8898;
    public static ObservableList<Player> players = FXCollections.observableArrayList();
    public static ObservableList<Game> games = FXCollections.observableArrayList();
    static EventLoopGroup group = new NioEventLoopGroup();


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
    private TextArea log_area;

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


            button_serv_stat.setText("ON");
            startServer();
        }else{
            button_serv_stat.setText("OFF");
            stopServer();
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
        text_filed_port.setText(""+port);

        tv_game_id.setCellValueFactory( new  PropertyValueFactory<>("Id"));
        tv_game_player1.setCellValueFactory( new  PropertyValueFactory<>("login1"));
        tv_game_player2.setCellValueFactory( new  PropertyValueFactory<>("login2"));
        tv_game_score.setCellValueFactory( new  PropertyValueFactory<>("score"));
        tv_game_status.setCellValueFactory( new  PropertyValueFactory<>("status"));


        tv_player_id.setCellValueFactory( new  PropertyValueFactory<>("Id"));
        tv_player_ip.setCellValueFactory( new  PropertyValueFactory<>("Ip"));
        tv_player_login.setCellValueFactory( new  PropertyValueFactory<>("login"));
        tv_player_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableview_players.setItems(players);
        tableview_games.setItems(games);


        Console console = new Console(log_area);
        PrintStream ps = new PrintStream(console, false);


        System.setOut(ps);
        System.setErr(ps);

    }

    public void setLog_area(String string) {
        this.log_area.appendText(string);
    }

    public TextArea getLog_area() {
        return log_area;
    }

    public Integer getPort() {
        return Integer.parseInt(text_filed_port.getText());
    }



    public static class HelloServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf inBuffer = (ByteBuf) msg;

            String received = inBuffer.toString(CharsetUtil.UTF_8);
            System.out.println("Server received: " + received);
            response(received,ctx);





        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
            //.addListener(ChannelFutureListener.CLOSE);
            //  System.out.println("channelReadComplete");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            for(int i=0;i<players.size();i++){
                if(players.get(i).getCtx()==ctx){
                    players.remove(i);
                }
            }
            ctx.close();
            System.out.println("HelloServerHandler exceptionCaught");
        }

        void response(String response,ChannelHandlerContext ctx){

            List<String> list = new LinkedList<String>(Arrays.asList(response.replace(" ", "").trim().split("\t")));
            System.out.println(Arrays.toString(list.toArray()));
            if(list.size()<2){
                System.out.println("undefinited response");
            }
            switch (list.get(1)) {
                case "LOGIN":
                    boolean exist_player=false;
                    for(int i=0;i<players.size();i++){
                        if(players.get(i).getLogin().equals(list.get(0))){
                            exist_player=true;
                        }
                    }
                    if(exist_player){
                        ctx.write(Unpooled.copiedBuffer("\tLOGIN\tERROR", CharsetUtil.UTF_8));
                    }else{
                        players.add(new Player(players.size()+1,list.get(0),list.get(2),ctx));
                        //      Controler.players.get(0).setIp("fdfdf"+Controler.players.size());
                        //      Controler.players.get(0).setStatus("fdfdf");
                        ctx.write(Unpooled.copiedBuffer("\tLOGIN\tOK", CharsetUtil.UTF_8));
                    }



                    break;
                case "LOGOUT":

                    //    Controler.players.get(0).setIp("we"+Controler.players.size());
                    //    Controler.players.get(0).setStatus("fdwewefdf");
                    boolean logget_player=false;
                    for(int i=0;i<players.size();i++){
                        if(players.get(i).getLogin().equals(list.get(0))){
                            players.remove(i);
                            logget_player=true;
                            break;
                        }
                    }
                    if(logget_player){
                        ctx.writeAndFlush(Unpooled.copiedBuffer("\tLOGOUT\tOK", CharsetUtil.UTF_8));
                    }else{
                        ctx.writeAndFlush(Unpooled.copiedBuffer("\tLOGOUT\tERROR", CharsetUtil.UTF_8));
                    }


                    break;

                case "GET_PLAYERS":

                    //    Controler.players.get(0).setIp("wfwefe"+Controler.players.size());
                    //    Controler.players.get(0).setStatus("wfewefwf");

                    StringBuilder builder = new StringBuilder();



                    for(int i=0; i < players.size(); i++) {
                        builder.append("\t"+players.get(i).getLogin()+"\t"+players.get(i).getIp()+"\t"+players.get(i).getStatus());
                    }
                    ctx.write(Unpooled.copiedBuffer("\tGET_PLAYERS"+builder.toString(), CharsetUtil.UTF_8));

                    break;

                case "INVITE":
                    Player player_1=null;
                    for(int i=0;i<players.size();i++){
                        player_1= players.get(i);
                        if(player_1.getLogin().equals(list.get(2))){
                            break;
                        }
                    }
                    if(player_1!=null){
                        if( player_1.getCtx()!=null){
                            player_1.getCtx().writeAndFlush(Unpooled.copiedBuffer("\tINVITE\t"+list.get(0), CharsetUtil.UTF_8));
                        }else{
                            System.out.println("player.getCtx() = null");
                        }

                    }

                    break;
                case "INVITE_RESPONSE":
                    player_1 = null;
                    for(int i=0;i<players.size();i++){
                        if(players.get(i).getLogin().equals(list.get(2))){
                            player_1=players.get(i);
                            players.get(0).setStatus("dscdcs");
                            break;
                        }
                    }
                    if(player_1!=null){

                        if( player_1.getCtx()!=null){
                            if(list.get(3).equals("OK")){

                                player_1.getCtx().writeAndFlush(Unpooled.copiedBuffer("\tINVITE_RESPONSE\t"+list.get(2)+"\t"+"OK"+"\t"+(games.size() + 1), CharsetUtil.UTF_8));
                                ctx.writeAndFlush(Unpooled.copiedBuffer("\tINVITE_RESPONSE\t"+list.get(2)+"\t"+"OK"+"\t"+(games.size() + 1), CharsetUtil.UTF_8));
                                Player player_2=null;
                                for(int i=0;i<players.size();i++){
                                    player_2= players.get(i);

                                    if(player_2.getCtx()==ctx){
                                        player_1.setStatus("InGame");
                                        player_2.setStatus("InGame");
                                        games.add(new Game(games.size() + 1, player_1, player_2, (player1, player2, pos_player1, pos_player2, pos_ball_x, pos_ball_y, score_player1, score_player2) -> {
                                            Thread thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    player1.getCtx().writeAndFlush(Unpooled.copiedBuffer("\tGAME_FRAME\t"+pos_player1+"\t"+pos_player2+"\t"+pos_ball_x+"\t"+pos_ball_y+"\t"+score_player1+"\t"+score_player2, CharsetUtil.UTF_8));
                                                }
                                            });
                                            thread.start();
                                            player2.getCtx().writeAndFlush(Unpooled.copiedBuffer("\tGAME_FRAME\t"+pos_player1+"\t"+pos_player2+"\t"+pos_ball_x+"\t"+pos_ball_y+"\t"+score_player1+"\t"+score_player2, CharsetUtil.UTF_8));
                                            //       System.out.println("GAME_FRAME\t"+pos_player1+"\t"+pos_player2+"\t"+pos_ball_x+"\t"+pos_ball_y+"\t"+score_player1+"\t"+score_player2);
                                        }));
                                        System.out.println("added game : "+(games.size()+1)+" "+player_1.getLogin()+" "+player_2.getLogin());
                                        break;
                                    }
                                }


                            }else{

                                player_1.getCtx().writeAndFlush(Unpooled.copiedBuffer("\tINVITE_RESPONSE\t"+list.get(2)+"\t"+"CANCEL", CharsetUtil.UTF_8));

                            }

                        }else{
                            System.out.println("player.getCtx() = null");
                        }

                    }

                    break;
                case "END_GAME":
                    Game game= games.get(Integer.parseInt(list.get(2))-1);
                    game.setStatus("GameEnd");
                    game.getPlayer1().getCtx().writeAndFlush(Unpooled.copiedBuffer("\tEND_GAME", CharsetUtil.UTF_8));
                    game.getPlayer2().getCtx().writeAndFlush(Unpooled.copiedBuffer("\tEND_GAME", CharsetUtil.UTF_8));
                    game.getPlayer1().setStatus("Active");
                    game.getPlayer2().setStatus("Active");
                    game.destroyGame();
                    games.remove(Integer.parseInt(list.get(2))-1);
                    break;
                case "UP":

                    game = games.get(Integer.parseInt(list.get(2)) - 1);
                    game.moveUp(list.get(0));
                    break;
                case "DOWN":
                    game = games.get(Integer.parseInt(list.get(2)) - 1);
                    game.moveDown(list.get(0));
                    break;
                case "STOP":
                    game = games.get(Integer.parseInt(list.get(2)) - 1);
                    game.moveStop(list.get(0));
                    break;

                default:
                    System.out.println("undefinited command");
                    break;
            }
        }
    }

    void startServer(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try{

                    ServerBootstrap serverBootstrap = new ServerBootstrap();
                    serverBootstrap.group(group);
                    serverBootstrap.channel(NioServerSocketChannel.class);



                    //serverBootstrap.localAddress(new InetSocketAddress("localhost", getPort()));
                    serverBootstrap.localAddress(new InetSocketAddress(Inet4Address.getLocalHost().getHostAddress(), getPort()));

                    serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HelloServerHandler());
                        }
                    });

                    ChannelFuture channelFuture = serverBootstrap.bind().sync();
                    System.out.println("Server ON");
                    channelFuture.channel().closeFuture().sync();

                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Server exception");
                } finally {
                    try {
                        group.shutdownGracefully().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Server OFF");
                }
            }
        };
        thread.start();

    }
    void stopServer(){
        try {
            players.clear();
            group.shutdownGracefully().sync();
            group.shutdown();
            group = new NioEventLoopGroup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    output.appendText(String.valueOf((char) i));
                }
            });


            //output.appendText(String.valueOf((char) i));
        }
    }
}