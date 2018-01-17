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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Main extends Application {

    private static Main instance;
    Stage window;
    static int port = 8898;

    static EventLoopGroup group = new NioEventLoopGroup();

    @Override
    public void start(Stage primaryStage) throws Exception {
        window= primaryStage;
        window.setTitle("Game Server");
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("root_view.fxml"))));
        window.show();
        window.setResizable(false);




    }


    public static void main(String[] args) throws InterruptedException {

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
                    serverBootstrap.localAddress(new InetSocketAddress("localhost", port));
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
                    for(int i=0;i<Controler.players.size();i++){
                        if(Controler.players.get(i).getLogin().equals(list.get(0))){
                            exist_player=true;
                        }
                    }
                    if(exist_player){
                        ctx.write(Unpooled.copiedBuffer("LOGIN\tERROR", CharsetUtil.UTF_8));
                    }else{
                        Controler.addPlayer(new Player(Controler.getSizePlayer()+1,list.get(0),list.get(2),ctx));
                        ctx.write(Unpooled.copiedBuffer("LOGIN\tOK", CharsetUtil.UTF_8));
                    }

                    break;
                case "GET_PLAYERS":

                    StringBuilder builder = new StringBuilder();

                    for(int i=0; i < Controler.players.size(); i++) {
                        builder.append("\t"+Controler.players.get(i).getLogin()+"\t"+Controler.players.get(i).getIp()+"\t"+Controler.players.get(i).getStatus());
                    }
                    ctx.write(Unpooled.copiedBuffer("GET_PLAYERS"+builder.toString(), CharsetUtil.UTF_8));

                    break;

                case "INVITE":
                    Player player=null;
                    for(int i=0;i<Controler.players.size();i++){
                        player= Controler.players.get(i);
                        if(player.getLogin().equals(list.get(2))){
                            break;
                        }
                    }
                    if(player!=null){
                        System.out.println("traing to invite player:"+player.getLogin());
                        //ctx.write(Unpooled.copiedBuffer("traing to invite to invite", CharsetUtil.UTF_8));
                        if( player.getCtx()!=null){
                            player.getCtx().writeAndFlush(Unpooled.copiedBuffer("INVITE\t"+list.get(2), CharsetUtil.UTF_8));
                        }else{
                            System.out.println("player.getCtx() = null");
                        }

                    }

                    break;
                case "INVITE_RESPONSE":
                    player = null;
                    for(int i=0;i<Controler.players.size();i++){
                        player= Controler.players.get(i);
                        if(player.getLogin().equals(list.get(2))){
                            break;
                        }
                    }
                    if(player!=null){

                        //ctx.write(Unpooled.copiedBuffer("traing to invite to invite", CharsetUtil.UTF_8));
                        if( player.getCtx()!=null){
                            if(list.get(3).equals("OK")){

                                player.getCtx().writeAndFlush(Unpooled.copiedBuffer("INVITE_RESPONSE\t"+list.get(2)+"\t"+"OK", CharsetUtil.UTF_8));
                            }else{

                                player.getCtx().writeAndFlush(Unpooled.copiedBuffer("INVITE_RESPONSE\t"+list.get(2)+"\t"+"CANCEL", CharsetUtil.UTF_8));

                            }

                        }else{
                            System.out.println("player.getCtx() = null");
                        }

                    }

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
}
