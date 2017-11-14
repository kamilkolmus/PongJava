package sample;


import javafx.animation.AnimationTimer;

interface GameInterface {

    void ballPosition(int x, int y);

    void positionPlayer1(int y);

    void positionPlayer2(int y);

    void gameScore(int player1Score, int player2Score);


}

enum PLAYERS {

    TWO_PLAYERS, VS_BOT

}

enum PLAYER_MOVE {

    MOVE_UP, MOVE_DOWN , MOVE_STOP

}

public class GameEngine {

    private GameInterface gameInterface;

    AnimationTimer timer;

    private boolean botControl;

    private int WIDTH , HEIGHT ;
    private int speedX = 3, speedY = 3, dx = speedX, dy = speedY;
    private int scorePlayer = 0, scoreBot = 0;

    private int ballX, ballY;
    private int player1Y, player2Y;
    private PLAYER_MOVE player1_isMoving,player2_isMoving;
    private int playerSizeX, playerSizeY;
    private int player_move_step=5;
    private double ballradius;


    GameEngine(PLAYERS players, GameInterface gameInterface, int WIDTH, int HEIGHT, int playerSizeX, int playerSizeY,double ballradius) {

        this.gameInterface = gameInterface;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;


        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        this.ballradius=ballradius;

        player1Y = HEIGHT / 2;
        player2Y = HEIGHT / 2;

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
        System.out.print(""+speedX+"\n\r");
        double x = ballX, y = ballY;
        //move player1
        if(player1_isMoving==PLAYER_MOVE.MOVE_UP){
            if(player1Y>player_move_step){
                player1Y = player1Y - player_move_step;
            }else{
                player1Y=0;
            }
        }else if(player1_isMoving==PLAYER_MOVE.MOVE_DOWN){

            if(player1Y<HEIGHT-playerSizeY-player_move_step){
                player1Y = player1Y + player_move_step;
            }else{
                player1Y=HEIGHT-playerSizeY;
            }

        }
        //move bot/player2
        if (botControl) {
            if ( dx > 0 && player2Y + 20 > y) moveBOT(PLAYER_MOVE.MOVE_UP);
            else if (dx > 0 && player2Y + playerSizeY - 20 < y) moveBOT(PLAYER_MOVE.MOVE_DOWN);
            else moveBOT(PLAYER_MOVE.MOVE_STOP);

        }
        if(player2_isMoving==PLAYER_MOVE.MOVE_UP){
            if(player2Y>player_move_step){
                player2Y = player2Y - player_move_step;
            }else{
                player2Y=0;
            }
        }else if(player2_isMoving==PLAYER_MOVE.MOVE_DOWN){

            if(player2Y<HEIGHT-playerSizeY-player_move_step){
                player2Y = player2Y + player_move_step;
            }else{
                player2Y=HEIGHT-playerSizeY;
            }
        }

        //update ball
        ballX = ballX + dx;
        ballY = ballY + dy;

        gameInterface.ballPosition(ballX, ballY);


        if (x>0&&x <= playerSizeX + 10 && y > player1Y && y < player1Y + playerSizeY) {

            if(dx != speedX){
                speedX++;
            }
            dx = speedX;



        }

        if (x<WIDTH && x >= WIDTH - (playerSizeX + 10) && y > player2Y && y < player2Y + playerSizeY) {
            if(dx != -speedX) {
                speedX++;
            }
            dx = -speedX;
        }
        if (speedX > playerSizeX+12) {
            speedX = playerSizeX+12;
        }

        if (x < -50) {

            scoreBot++;
            gameInterface.gameScore(scorePlayer, scoreBot);
            ballX = WIDTH / 2;
            ballY = (HEIGHT / 2);
            speedX = 3;
            dx = speedX;
        }
        if (x > WIDTH + 50) {

            scorePlayer++;
            gameInterface.gameScore(scorePlayer, scoreBot);
            ballX = WIDTH / 2;
            ballY = (HEIGHT / 2);
            speedX = 3;
            dx = speedX;
        }


        if (y <= ballradius/2) dy = speedY;
        if (y >= HEIGHT - ballradius/2) dy = -speedY;




        //botapdate


        gameInterface.positionPlayer1(player1Y);
        gameInterface.positionPlayer2(player2Y);
    }

    void movePlayer_1(PLAYER_MOVE move) {

        switch (move) {
            case MOVE_UP:

                player1_isMoving=PLAYER_MOVE.MOVE_UP;

                break;
            case MOVE_DOWN:
                player1_isMoving=PLAYER_MOVE.MOVE_DOWN;

                break;
            case MOVE_STOP:
                player1_isMoving=PLAYER_MOVE.MOVE_STOP;
            default:
        }
    }


    void movePlayer_2(PLAYER_MOVE move) {

        if (botControl == false) {
            switch (move) {
                case MOVE_UP:
                    player2_isMoving=PLAYER_MOVE.MOVE_UP;

                    break;
                case MOVE_DOWN:
                    player2_isMoving=PLAYER_MOVE.MOVE_DOWN;

                    break;
                case MOVE_STOP:
                    player2_isMoving=PLAYER_MOVE.MOVE_STOP;
                default:
            }
        }
    }

    private void moveBOT(PLAYER_MOVE move) {


            switch (move) {
                case MOVE_UP:
                    player2_isMoving=PLAYER_MOVE.MOVE_UP;

                    break;
                case MOVE_DOWN:
                    player2_isMoving=PLAYER_MOVE.MOVE_DOWN;

                    break;
                case MOVE_STOP:
                    player2_isMoving=PLAYER_MOVE.MOVE_STOP;
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
