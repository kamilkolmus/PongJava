package com.pong.serwer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;




public class Main extends Application {

    public static ObservableList<Player> players = FXCollections.observableArrayList();
    public static ObservableList<Game> games = FXCollections.observableArrayList();

    private static Main instance;
    Stage window;
    static int port = 8898;

     EventLoopGroup group = new NioEventLoopGroup();

    @Override
    public void start(Stage primaryStage) throws Exception {
        window= primaryStage;
        window.setTitle("Game Server");
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("root_view.fxml"))));
        window.show();
        window.setResizable(false);
    }


    public static void main(String[] args) throws InterruptedException {
        launch(args);
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
            ctx.close();
            System.out.println("exceptionCaught");
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
                        ctx.write(Unpooled.copiedBuffer("LOGIN\tERROR", CharsetUtil.UTF_8));
                    }else{
                        players.add(new Player(players.size()+1,list.get(0),list.get(2),ctx));
                  //      Controler.players.get(0).setIp("fdfdf"+Controler.players.size());
                  //      Controler.players.get(0).setStatus("fdfdf");
                        ctx.write(Unpooled.copiedBuffer("LOGIN\tOK", CharsetUtil.UTF_8));
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
                        ctx.writeAndFlush(Unpooled.copiedBuffer("LOGOUT\tOK", CharsetUtil.UTF_8));
                    }else{
                        ctx.writeAndFlush(Unpooled.copiedBuffer("LOGOUT\tERROR", CharsetUtil.UTF_8));
                    }


                    break;

                case "GET_PLAYERS":

                //    Controler.players.get(0).setIp("wfwefe"+Controler.players.size());
                //    Controler.players.get(0).setStatus("wfewefwf");

                    StringBuilder builder = new StringBuilder();



                    for(int i=0; i < players.size(); i++) {
                        builder.append("\t"+players.get(i).getLogin()+"\t"+players.get(i).getIp()+"\t"+players.get(i).getStatus());
                    }
                    ctx.write(Unpooled.copiedBuffer("GET_PLAYERS"+builder.toString(), CharsetUtil.UTF_8));

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
                        System.out.println("traing to invite player:"+player_1.getLogin());
                        if( player_1.getCtx()!=null){
                            player_1.getCtx().writeAndFlush(Unpooled.copiedBuffer("INVITE\t"+list.get(0), CharsetUtil.UTF_8));
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
                            break;
                        }
                    }
                    if(player_1!=null){

                        if( player_1.getCtx()!=null){
                            if(list.get(3).equals("OK")){

                                player_1.getCtx().writeAndFlush(Unpooled.copiedBuffer("INVITE_RESPONSE\t"+list.get(2)+"\t"+"OK"+"\t"+(games.size() + 1), CharsetUtil.UTF_8));
                                ctx.writeAndFlush(Unpooled.copiedBuffer("INVITE_RESPONSE\t"+list.get(2)+"\t"+"OK"+"\t"+(games.size() + 1), CharsetUtil.UTF_8));
                                Player player_2=null;
                                for(int i=0;i<players.size();i++){
                                    player_2= players.get(i);

                                    if(player_2.getCtx()==ctx){
                                        player_1.setStatus("InGame");
                                        player_2.setStatus("InGame");
                                        games.add(new Game(games.size() + 1, player_1, player_2, (player1, player2, pos_player1, pos_player2, pos_ball_x, pos_ball_y, score_player1, score_player2) -> {
                                            player1.getCtx().writeAndFlush(Unpooled.copiedBuffer("GAME_FRAME\t"+pos_player1+"\t"+pos_player2+"\t"+pos_ball_x+"\t"+pos_ball_y+"\t"+score_player1+"\t"+score_player2, CharsetUtil.UTF_8));
                                            player2.getCtx().writeAndFlush(Unpooled.copiedBuffer("GAME_FRAME\t"+pos_player1+"\t"+pos_player2+"\t"+pos_ball_x+"\t"+pos_ball_y+"\t"+score_player1+"\t"+score_player2, CharsetUtil.UTF_8));
                                     //       System.out.println("GAME_FRAME\t"+pos_player1+"\t"+pos_player2+"\t"+pos_ball_x+"\t"+pos_ball_y+"\t"+score_player1+"\t"+score_player2);
                                        }));
                                        System.out.println("added game : "+(games.size()+1)+" "+player_1.getLogin()+" "+player_2.getLogin());
                                        break;
                                    }
                                }


                            }else{

                                player_1.getCtx().writeAndFlush(Unpooled.copiedBuffer("INVITE_RESPONSE\t"+list.get(2)+"\t"+"CANCEL", CharsetUtil.UTF_8));

                            }

                        }else{
                            System.out.println("player.getCtx() = null");
                        }

                    }

                    break;
                case "END_GAME":
                    Game game= games.get(Integer.parseInt(list.get(2))-1);
                    game.setStatus("GameEnd");
                    players.get(0).setIp("wedfd"+players.size());
                    game.getPlayer1().getCtx().writeAndFlush(Unpooled.copiedBuffer("END_GAME", CharsetUtil.UTF_8));
                    game.getPlayer2().getCtx().writeAndFlush(Unpooled.copiedBuffer("END_GAME", CharsetUtil.UTF_8));
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
                    System.out.println("Serwer 1");
                    ServerBootstrap serverBootstrap = new ServerBootstrap();
                    serverBootstrap.group(group);
                    serverBootstrap.channel(NioServerSocketChannel.class);
                    System.out.println("Serwer localaddres");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("root_view.fxml"));
                    loader.load();
                    Controler controler = loader.getController();

                    serverBootstrap.localAddress(new InetSocketAddress("localhost", controler.getPort()));
                    System.out.println("Serwer childHandler");
                    serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HelloServerHandler());
                        }
                    });
                    System.out.println("Serwer channelFuture");
                    ChannelFuture channelFuture = serverBootstrap.bind().sync();
                    System.out.println("Serwer channelFuture2");
                    channelFuture.channel().closeFuture().sync();
                    System.out.println("Serwer 2");
                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Serwer exception");
                } finally {
                    try {
                        group.shutdownGracefully().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Serwer finally");
                }
            }
        };
        thread.start();

    }
    void stopServer(){
        try {
            group.shutdownGracefully().sync();
            group.shutdown();
            group = new NioEventLoopGroup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Main() {
        instance = this;
    }

    static Main getInstance() {
        return instance;
    }
}
