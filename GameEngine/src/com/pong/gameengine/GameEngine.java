package com.pong.gameengine;


import javafx.animation.AnimationTimer;

public class GameEngine {

    private GameInterface gameInterface;

    AnimationTimer timer;

    private boolean botControl;

    private int WIDTH , HEIGHT ;
    private int speedX = 4, speedY = 0, dx = speedX, dy = speedY;
    private int scorePlayer = 0, scoreBot = 0;

    private double ballX, ballY;
    private double player1Y, player2Y;
    private PLAYER_MOVE player1_move, player2_move;

    private int playerSizeX, playerSizeY;
    private int player_move_step=10;
    private double ballradius;


    public GameEngine(PLAYERS players, GameInterface gameInterface, int WIDTH, int HEIGHT, int playerSizeX, int playerSizeY, double ballradius) {

        this.gameInterface = gameInterface;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;


        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        this.ballradius=ballradius;

     //   player1Y = HEIGHT / 2 - playerSizeY/2;
        player1Y=2;
        player2Y = HEIGHT / 2 - playerSizeY/2;

        this.playerSizeX = playerSizeX;
        this.playerSizeY = playerSizeY;

        if (players == PLAYERS.TWO_PLAYERS) {
            botControl = false;
        } else {
            botControl = true;
        }
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameUpdate();
            }
        };
        // timer.handle(1);

    }

    private void gameUpdate() {
        System.out.print(""+speedY+"\n\r");
        double x = ballX, y = ballY;

        //player1 reflection
        if (x>0&&x <= (playerSizeX + playerSizeX*2/4) && y > player1Y && y < player1Y + playerSizeY) {

            if(dx != speedX){
                speedX++;
                if(player1_move!=PLAYER_MOVE.MOVE_STOP){
                    if(player1_move==PLAYER_MOVE.MOVE_UP){
                        if(dy>0){speedY=((speedY)/2); dy = speedY;}
                        else {speedY=(speedY+2);dy = -speedY;}
                    }else{
                        if(dy>=0){speedY=(speedY+2);dy = speedY;}
                        else {speedY=((speedY)/2);dy = -speedY;}
                    }

                }
            }
            dx = speedX;



        }

        //player2 reflection
        if (x<WIDTH && x >= WIDTH - (playerSizeX + playerSizeX*2/4) && y > player2Y && y < player2Y + playerSizeY) {
            if(dx != -speedX) {
                speedX++;
                if(player2_move!=PLAYER_MOVE.MOVE_STOP){
                    if(player2_move==PLAYER_MOVE.MOVE_UP){
                        if(dy>0){speedY=((speedY)/2);dy = speedY;}
                        else {speedY=(speedY+2);dy =- speedY;}
                    }else{
                        if(dy>=0){speedY=(speedY+2); dy = speedY;}
                        else {speedY=((speedY)/2); dy = -speedY;}
                    }


                }
            }
            dx = -speedX;


        }
        if (speedX > playerSizeX) {
            speedX = playerSizeX;
        }

        if (x < -150) {

            scoreBot++;
            gameInterface.gameScore(scorePlayer, scoreBot);
            ballX = WIDTH / 2;
            ballY = (HEIGHT / 2);
            speedX = 4;
            dx = speedX;
            speedY=3;
            dy = speedY;

        }
        if (x > WIDTH + 150) {

            scorePlayer++;
            gameInterface.gameScore(scorePlayer, scoreBot);
            ballX = WIDTH / 2;
            ballY = (HEIGHT / 2);
            speedX = 4;
            dx = -speedX;
            speedY=3;
            dy = -speedY;
        }




        if (y <= ballradius/2) dy = speedY;
        if (y >= HEIGHT - ballradius/2) dy = -speedY;

        //update ball position
        ballX = ballX + dx;
        ballY = ballY + dy;

        //move player1
        if(player1_move ==PLAYER_MOVE.MOVE_UP){
            if(player1Y>player_move_step){
                player1Y = player1Y - player_move_step;
            }else{
                player1Y=0;
                player1_move=PLAYER_MOVE.MOVE_STOP;
            }
        }else if(player1_move ==PLAYER_MOVE.MOVE_DOWN){

            if(player1Y<HEIGHT-playerSizeY-player_move_step){
                player1Y = player1Y + player_move_step;
            }else{
                player1Y=HEIGHT-playerSizeY;
                player1_move=PLAYER_MOVE.MOVE_STOP;
            }

        }
        //move bot/player2
        if (botControl) {
            if ( dx > 0 && player2Y + 20 > y) moveBOT(PLAYER_MOVE.MOVE_UP);
            else if (dx > 0 && player2Y + playerSizeY - 20 < y) moveBOT(PLAYER_MOVE.MOVE_DOWN);
            else moveBOT(PLAYER_MOVE.MOVE_STOP);

        }
        if(player2_move ==PLAYER_MOVE.MOVE_UP){
            if(player2Y>player_move_step){
                player2Y = player2Y - player_move_step;
            }else{
                player2Y=0;
                player2_move=PLAYER_MOVE.MOVE_STOP;
            }
        }else if(player2_move ==PLAYER_MOVE.MOVE_DOWN){

            if(player2Y<HEIGHT-playerSizeY-player_move_step){
                player2Y = player2Y + player_move_step;
            }else{
                player2Y=HEIGHT-playerSizeY;
                player2_move=PLAYER_MOVE.MOVE_STOP;
            }
        }


        //updates
        gameInterface.ballPosition(ballX, ballY);
        gameInterface.positionPlayer1(player1Y);
        gameInterface.positionPlayer2(player2Y);
    }

    public void movePlayer_1(PLAYER_MOVE move) {

        switch (move) {
            case MOVE_UP:

                player1_move =PLAYER_MOVE.MOVE_UP;

                break;
            case MOVE_DOWN:
                player1_move =PLAYER_MOVE.MOVE_DOWN;

                break;
            case MOVE_STOP:
                player1_move =PLAYER_MOVE.MOVE_STOP;
            default:
        }
    }


    public void movePlayer_2(PLAYER_MOVE move) {

        if (botControl == false) {
            switch (move) {
                case MOVE_UP:
                    player2_move =PLAYER_MOVE.MOVE_UP;

                    break;
                case MOVE_DOWN:
                    player2_move =PLAYER_MOVE.MOVE_DOWN;

                    break;
                case MOVE_STOP:
                    player2_move =PLAYER_MOVE.MOVE_STOP;
                default:
            }
        }
    }

    private void moveBOT(PLAYER_MOVE move) {


            switch (move) {
                case MOVE_UP:
                    player2_move =PLAYER_MOVE.MOVE_UP;

                    break;
                case MOVE_DOWN:
                    player2_move =PLAYER_MOVE.MOVE_DOWN;

                    break;
                case MOVE_STOP:
                    player2_move =PLAYER_MOVE.MOVE_STOP;
                default:
            }

    }


    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void destroy() {
        timer = null;
    }
}
