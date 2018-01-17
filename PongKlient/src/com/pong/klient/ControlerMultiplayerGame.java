package com.pong.klient;

import com.pong.gameengine.PLAYERS;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.Inet4Address;
import java.net.UnknownHostException;


public class ControlerMultiplayerGame {





    @FXML
    void onLocalMultiplayerClick(ActionEvent event) {

        Main.getInstance().setSceneGameMultiplayerLocal();


    }


    public void onNetworkMultiplayerClick(ActionEvent actionEvent) {

        Main.getInstance().setSceneGameMultiplayerNetwork();

    }
    public void onReturnClick(ActionEvent actionEvent) {

        Main.getInstance().setSceneGameModeSelect();

    }



}
