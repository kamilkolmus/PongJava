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

            String[] parts = received.split(":");
//            for(int i=0;i<parts.length;i++){
//                System.out.println(parts[i]);
//
//            }
            if(parts[0].equals("login")){
                System.out.println("adding player");
                Controler.addPlayer(new Player(Controler.getSizePlayer()+1,parts[1],parts[2]));
                ctx.write(Unpooled.copiedBuffer("login:ok", CharsetUtil.UTF_8));
            }else{
                ctx.write(Unpooled.copiedBuffer(parts[0]=":error", CharsetUtil.UTF_8));
            }



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
    }
}
