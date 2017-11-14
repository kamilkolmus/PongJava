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

    MOVE_UP, MOVE_DOWN

}

public class GameEngine {

    private GameInterface gameInterface;

    AnimationTimer timer;

    private boolean botControl;

    private int WIDTH = 1000, HEIGHT = 400;
    private int speedX = 3, speedY = 2, dx = speedX, dy = speedY;
    private int scorePlayer = 0, scoreBot = 0;

    private int ballX, ballY;
    private int player1Y, player2Y;
    private int playerSizeX, playerSizeY;

    GameEngine(PLAYERS players, GameInterface gameInterface, int WIDTH, int HEIGHT, int playerSizeX, int playerSizeY) {

        this.gameInterface = gameInterface;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;

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
        double x = ballX, y = ballY;
        System.out.println("x=" + x + " y=" + y);

        if (x>0&&x <= playerSizeX + 5 && y > player1Y && y < player1Y + playerSizeY) {

            dx = speedX;
            speedX++;


        }

        if (x<WIDTH && x >= WIDTH - (playerSizeX + 5) && y > player2Y && y < player2Y + playerSizeY) {
            dx = -speedX;
            speedX++;
        }
        if (speedX > playerSizeX) {
            speedX = playerSizeX;
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


        if (y <= 0) dy = speedY;
        if (y >= HEIGHT - 5) dy = -speedY;


        //update ball
        ballX = ballX + dx;
        ballY = ballY + dy;

        gameInterface.ballPosition(ballX, ballY);

        //botapdate
        if (botControl) {
            if (x > WIDTH / 2 && dx > 0 && player2Y + 20 > y) player2Y = player2Y - 5;
            if (x > WIDTH / 2 && dx > 0 && player2Y + playerSizeY - 20 < y) player2Y = player2Y + 5;
        }


        gameInterface.positionPlayer1(player1Y);
        gameInterface.positionPlayer2(player2Y);
    }

    void movePlayer_1(PLAYER_MOVE move) {

        switch (move) {
            case MOVE_UP:
                player1Y = player1Y - 40;
                break;
            case MOVE_DOWN:
                player1Y = player1Y + 40;
                break;
            default:
        }
    }

    void movePlayer_2(PLAYER_MOVE move) {

        if (botControl == false) {
            switch (move) {
                case MOVE_UP:
                    player2Y = player2Y - 40;
                    break;
                case MOVE_DOWN:
                    player2Y = player2Y + 40;
                    break;
                default:
            }
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
