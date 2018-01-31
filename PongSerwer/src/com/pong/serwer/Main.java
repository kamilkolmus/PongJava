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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;




public class Main extends Application {



    private static Main instance;
    Stage window;




    @Override
    public void start(Stage primaryStage) throws Exception {
        window= primaryStage;
        window.setTitle("Game Server");
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("root_view.fxml"))));
        window.show();
        window.setResizable(false);
        window.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Controler.group.shutdownGracefully();
                        Controler.isSerwerRunning=false;
                    }
                });
            }
        });




    }


    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }


    public Main() {
        instance = this;
    }

    static Main getInstance() {
        return instance;
    }


}
