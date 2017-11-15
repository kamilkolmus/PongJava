package com.pong.gameengine;

public interface GameInterface {

    void ballPosition(int x, int y);

    void positionPlayer1(int y);

    void positionPlayer2(int y);

    void gameScore(int player1Score, int player2Score);


}
