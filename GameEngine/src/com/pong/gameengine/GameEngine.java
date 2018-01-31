package com.pong.gameengine;


import javafx.animation.AnimationTimer;

public class GameEngine {

    private GameInterface gameInterface;

    AnimationTimer timer;

    private boolean botControl;

    private int WIDTH , HEIGHT ;
    private final double  speedx_final = 10, speedy_final = 0, speed_final=10,dspeed_final=0.5;


    private double speedx = speedx_final, speedy = speedy_final, speed=speed_final;
    private double dspeed=dspeed_final;
    private double refl_engle_norm =0.66; // z normalizowany kąt odbicia podczas ruchu gracza  val=od 0 do 1
    private double speedy_max_norm =0.96; // z normalizowane ograniczonie prędkosci po osi Y val=od 0 do 1
    private int scorePlayer = 0, scoreBot = 0;

    private double ballX, ballY;
    private double player1Y, player2Y;
    private PlayerMove player1_move, player2_move;

    private int playerSizeX, playerSizeY;
    private int player1_move_step =0;
    private int player2_move_step =0;
    private double player_slow=0.1;

    private int player_move_step_counst=10;
    private double ballradius;



    public GameEngine(NumberOfPlayers numberOfPlayers, GameInterface gameInterface, int WIDTH, int HEIGHT, int playerSizeX, int playerSizeY, double ballradius) {

        this.gameInterface = gameInterface;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;


        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        this.ballradius=ballradius;

        player1Y = HEIGHT / 2 - playerSizeY/2;
        player2Y = HEIGHT / 2 - playerSizeY/2;

        this.playerSizeX = playerSizeX;
        this.playerSizeY = playerSizeY;

        if (numberOfPlayers == NumberOfPlayers.TWO_PLAYERS) {
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

    }

    private void gameUpdate() {
       // System.out.print("speedx="+ speedx +"  "+"speedy="+ speedy +"  "+"speed="+ speed +"  "+"speed_cont="+ Math.sqrt(speedx*speedx+speedy*speedy) +"  "+"\n\r");


        //player1 reflection
        if (ballX>0&&ballX <= (playerSizeX - speedx) && ballY > player1Y -ballradius&& ballY < player1Y + playerSizeY+ballradius) {

            if(speedx<0) {
             //   System.out.println("odbicie\n\r");

                speedx = (-1) * speedx / speed * (speed + dspeed);
                speedy = speedy / speed * (speed + dspeed);
                speed+=dspeed;
                ballX=playerSizeX;

              if(player1_move== PlayerMove.MOVE_UP){

                      speedy -= ((refl_engle_norm )/(speedy_max_norm))*speedy+(refl_engle_norm *speed);
                      speedx = Math.sqrt(speed*speed- speedy * speedy);


              }else if(player1_move== PlayerMove.MOVE_DOWN){

                      speedy += -((refl_engle_norm )/( speedy_max_norm))*speedy+(refl_engle_norm *speed);
                      speedx = Math.sqrt(speed*speed- speedy * speedy);


              }
            }
        }

        //player2 reflection
        if (ballX<WIDTH && ballX >= WIDTH - (playerSizeX + speedx) && ballY > player2Y -ballradius && ballY < player2Y + playerSizeY+ballradius) {
            if(speedx>0) {
                //  System.out.println("odbicie\n\r");

                speedx = (-1) * speedx / speed * (speed + dspeed);
                speedy = speedy / speed * (speed + dspeed);
                speed+=dspeed;
                ballX=WIDTH-playerSizeX;

                if(player2_move== PlayerMove.MOVE_UP){


                    speedy -=  ((refl_engle_norm *2*speed)/(2*speed* speedy_max_norm))*speedy+(refl_engle_norm *2*speed)/2;
                    speedx = -Math.sqrt(speed*speed- speedy * speedy);

                }else if(player2_move== PlayerMove.MOVE_DOWN){

                    speedy += -((refl_engle_norm *2*speed)/(2*speed* speedy_max_norm))*speedy+(refl_engle_norm *2*speed)/2;
                    speedx = -Math.sqrt(speed*speed- speedy * speedy);



                }
            }

        }

        if(speed>30){
            dspeed=0;
        }




        if (ballX < -150) {

            scoreBot++;
            gameInterface.gameScore(scorePlayer, scoreBot);
            ballX = WIDTH / 2;
            ballY = (HEIGHT / 2);
            speedx = speedx_final;
            speedy =speedy_final;
            speed=speed_final;
            dspeed=dspeed_final;

        }
        if (ballX > WIDTH + 150) {

            scorePlayer++;
            gameInterface.gameScore(scorePlayer, scoreBot);
            ballX = WIDTH / 2;
            ballY = (HEIGHT / 2);
            speedx =-speedx_final;
            speedy =speedy_final;
            speed=speed_final;
            dspeed=dspeed_final;
        }




        if (ballY <= ballradius) speedy = -speedy;
        if (ballY >= HEIGHT - ballradius) speedy = -speedy;

        //updzate ball position
        ballX = ballX + speedx;
        ballY = ballY + speedy;

        //move player1
        player1Y = player1Y + player1_move_step;
        if(player1_move == PlayerMove.MOVE_UP){
            if(player1Y> player1_move_step){
                player1_move_step =-player_move_step_counst;
            }else{
                player1Y=0;
                player1_move= PlayerMove.MOVE_STOP;
                player1_move_step =0;
            }
        }else if(player1_move == PlayerMove.MOVE_DOWN){
            if(player1Y<HEIGHT-playerSizeY- player1_move_step){
                player1_move_step =+player_move_step_counst;
            }else{
                player1Y=HEIGHT-playerSizeY;
                player1_move= PlayerMove.MOVE_STOP;
                player1_move_step =0;
            }
        }else if(player1_move == PlayerMove.MOVE_STOP){
            if(player1Y<= player1_move_step){
                player1Y=0;
                player1_move_step =0;
            }else if(player1Y>=HEIGHT-playerSizeY- player1_move_step){
                player1Y=HEIGHT-playerSizeY;
                player1_move_step =0;
            } else if(player1_move_step >0){
                player1_move_step -=player_slow;
            }else if(player1_move_step <0){
                player1_move_step +=player_slow;
            }

        }

        if (botControl) {
            if (speedy <= 0 && speedx > 0 && ballX>WIDTH*2/3 && player2Y  > ballY) moveBOT(PlayerMove.MOVE_UP);
            else if (speedy >= 0 && speedx > 0&& ballX>WIDTH*2/3  && player2Y+ playerSizeY  < ballY) moveBOT(PlayerMove.MOVE_DOWN);
            else if(speedx < 0&&player2Y+playerSizeY/2>HEIGHT/2+40){
                moveBOT(PlayerMove.MOVE_UP);
            }else if(speedx < 0&&player2Y+playerSizeY/2<HEIGHT/2-40){
                moveBOT(PlayerMove.MOVE_DOWN);
            }
            else moveBOT(PlayerMove.MOVE_STOP);
        }
        player2Y = player2Y + player2_move_step;
        if(player2_move == PlayerMove.MOVE_UP){
            if(player2Y> player2_move_step){
                player2_move_step =-player_move_step_counst;
            }else{
                player2Y=0;
                player2_move= PlayerMove.MOVE_STOP;
                player2_move_step =0;
            }
        }else if(player2_move == PlayerMove.MOVE_DOWN){
            if(player2Y<HEIGHT-playerSizeY- player2_move_step){
                player2_move_step =+player_move_step_counst;
            }else{
                player2Y=HEIGHT-playerSizeY;
                player2_move= PlayerMove.MOVE_STOP;
                player2_move_step =0;
            }
        }else if(player2_move == PlayerMove.MOVE_STOP){
            if(player2Y<= player2_move_step){
                player2Y=0;
                player2_move_step =0;
            }else if(player2Y>=HEIGHT-playerSizeY- player2_move_step){
                player2Y=HEIGHT-playerSizeY;
                player2_move_step =0;
            } else if(player2_move_step >0){
                player2_move_step -=player_slow;
            }else if(player2_move_step <0){
                player2_move_step +=player_slow;
            }

        }


        //updates
        gameInterface.ballPosition(ballX, ballY);
        gameInterface.positionPlayer1(player1Y);
        gameInterface.positionPlayer2(player2Y);
    }

    public void movePlayer_1(PlayerMove move) {

        switch (move) {
            case MOVE_UP:

                player1_move = PlayerMove.MOVE_UP;

                break;
            case MOVE_DOWN:
                player1_move = PlayerMove.MOVE_DOWN;

                break;
            case MOVE_STOP:
                player1_move = PlayerMove.MOVE_STOP;
            default:
        }
    }


    public void movePlayer_2(PlayerMove move) {

        if (botControl == false) {
            switch (move) {
                case MOVE_UP:
                    player2_move = PlayerMove.MOVE_UP;

                    break;
                case MOVE_DOWN:
                    player2_move = PlayerMove.MOVE_DOWN;

                    break;
                case MOVE_STOP:
                    player2_move = PlayerMove.MOVE_STOP;
                default:
            }
        }
    }

    private void moveBOT(PlayerMove move) {


            switch (move) {
                case MOVE_UP:
                    player2_move = PlayerMove.MOVE_UP;

                    break;
                case MOVE_DOWN:
                    player2_move = PlayerMove.MOVE_DOWN;

                    break;
                case MOVE_STOP:
                    player2_move = PlayerMove.MOVE_STOP;
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
