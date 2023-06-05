package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class Avatar extends Drawing implements Runnable{

    private final Image[] idle;
    private final Image[] run;
    private int frame = 0;
    private boolean isMoving;
    private boolean isFacingRight = true;
    private int lives;
    private Arm arm;
    public Avatar(){
        pos.setX(200);
        pos.setY(100);
        lives = 5;
        idle = new Image[8];
        for (int i = 1; i <= 8 ; i++) {
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("playerIdle/player-idle" + i + ".png")).getPath();
            idle[i-1] = new Image(uri);
        }

        run = new Image[4];
        for (int i = 1; i <= 4 ; i++) {
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("playerRun/player-run" + i + ".png")).getPath();
            run[i-1] = new Image(uri);
        }


    }

    @Override
    public void draw(GraphicsContext gc) {
        if(isFacingRight)
            gc.drawImage(
                    isMoving ? run[frame]:idle[frame], pos.getX()-25, pos.getY()-25, 50, 50);
        else
            gc.drawImage(isMoving ? run[frame]:idle[frame], pos.getX()+25, pos.getY()-25, -50, 50);
    }

    // Ejecutar en paralelo
    private boolean isAlive = true;
    @Override
    public void run() {
        while (isAlive) {
            frame = (frame+1)%4;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }

    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }

    public Arm getArm() {
        return arm;
    }

    public void setArm(Arm arm) {
        this.arm = arm;
    }
}
