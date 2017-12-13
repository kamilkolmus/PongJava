package com.pong.klient;


import com.pong.gameengine.PLAYERS;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


public class Main extends Application  {

    private static Main instance;
    Stage window;

    static ChannelHandlerContext channelHandlerContext;
    static String login="my_Login1";
    static int port = 8898;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window= primaryStage;
        window.setTitle("Game Pong: "+login);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_select_game_mode.fxml"))));
        window.show();
        window.setResizable(false);
        System.out.println("java version: "+System.getProperty("java.version"));
        System.out.println("javafx.version: " + System.getProperty("javafx.version"));

        EventLoopGroup group = new NioEventLoopGroup();
        Thread thread = new Thread(){
            public void run(){
                try{
                    Bootstrap clientBootstrap = new Bootstrap();

                    clientBootstrap.group(group);
                    clientBootstrap.channel(NioSocketChannel.class);
                    clientBootstrap.remoteAddress(new InetSocketAddress("localhost", port));
                    clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
                    ChannelFuture channelFuture = clientBootstrap.connect().sync();
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Klient exception");

                }finally {
                    try {
                        group.shutdownGracefully().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Klient disconected");
                }
            }
        };
        thread.start();


    }


    public static void main(String[] args) throws InterruptedException {



        launch(args);
    }

    void setSceneSettings(){
        try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_settings.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setSceneSinglePlayer(){

        GameView gameView = new GameView(PLAYERS.VS_BOT);
        window.setScene(new Scene(gameView));
        gameView.addListener(window);

    }

    void setSceneGameModeSelect(){
        try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("scene_select_game_mode.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

     void setSceneMultiPlayer() {

        GameView gameView = new GameView(PLAYERS.TWO_PLAYERS);
        window.setScene(new Scene(gameView));
        gameView.addListener(window);

    }
    public Main() {
        instance = this;
    }

    static Main getInstance() {
        return instance;
    }



    void moveUp() throws UnknownHostException {
        Main.channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(Main.login+"\t"+Inet4Address.getLocalHost().getHostAddress()+"\t"+"UP", CharsetUtil.UTF_8));

    }
    void moveDown() throws UnknownHostException {

        Main.channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(Main.login+"\t"+Inet4Address.getLocalHost().getHostAddress()+"\t"+"DOWN", CharsetUtil.UTF_8));
    }
    void moveStop() throws UnknownHostException {
        Main.channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(Main.login+"\t"+Inet4Address.getLocalHost().getHostAddress()+"\t"+"STOP", CharsetUtil.UTF_8));

    }

    public static class ClientHandler extends SimpleChannelInboundHandler {

        @Override
        public void channelActive(ChannelHandlerContext channelHandlerContext) throws UnknownHostException {
            Main.channelHandlerContext=channelHandlerContext;
            Main.channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("login:"+Main.login+":"+Inet4Address.getLocalHost().getHostAddress(), CharsetUtil.UTF_8));
        }


        public void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) {
            System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
            cause.printStackTrace();
            channelHandlerContext.close();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
            ByteBuf inBuffer = (ByteBuf) o;
            String received = inBuffer.toString(CharsetUtil.UTF_8);
            System.out.println("Client received: " + received);

        }
    }


}


