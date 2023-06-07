package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class ChasingEnemy extends Enemy {
    private final Image[] idleScorpion;
    private final Image[] runScorpion;
    private int frame = 0;
    private boolean isMoving = false;
    private int lives;

    public ChasingEnemy(Vector pos) {
        super(pos);
        idleScorpion = new Image[14];
        lives = 3;
        for (int i = 1; i <= 14; i++) {
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("scorpion-idle/scorpion-idle" + i + ".png")).getPath();
            idleScorpion[i - 1] = new Image(uri);
        }

        runScorpion = new Image[6];
        for (int i = 1; i <= 6; i++) {
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("scorpion-walk/scorpion-walk" + i + ".png")).getPath();
            runScorpion[i - 1] = new Image(uri);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(
                isMoving ? runScorpion[frame] : idleScorpion[frame], pos.getX() - 25, pos.getY() - 25, 50, 50);
    }

    public boolean isAlive = true;

    @Override
    public void run() {
        //Tercer plano
        while (isAlive) {
            isMoving = true;
            frame = (frame + 1) % 6;

            pos.setY(pos.getY());
            pos.setX(pos.getX());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
