package com.pong.gameengine;

public interface GameInterface {

    void ballPosition(double x, double y);

    void positionPlayer1(double y);

    void positionPlayer2(double y);

    void gameScore(int player1Score, int player2Score);


}
