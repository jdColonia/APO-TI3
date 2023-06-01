package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class Enemy extends Drawing implements Runnable{
    private final Image[] idleScorpion;
    private final Image[] runScorpion;
    private TypeEnemy typeEnemy;
    private int frame = 0;
    private boolean isMoving = false;
//    private boolean isFacingRight = true;
    public Enemy(Vector pos, TypeEnemy typeEnemy){
        this.typeEnemy = typeEnemy;
        this.pos = pos;
        idleScorpion = new Image[14];
        for (int i = 1; i <= 14 ; i++) {
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("scorpion-idle/scorpion-idle" + i + ".png")).getPath();
            idleScorpion[i-1] = new Image(uri);
        }

        runScorpion = new Image[6];
        for (int i = 1; i <= 6 ; i++) {
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("scorpion-walk/scorpion-walk" + i + ".png")).getPath();
            runScorpion[i-1] = new Image(uri);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
//        gc.setFill(Color.BLACK);
//        gc.setStroke(Color.RED);
//        gc.fillRect(pos.getX(), pos.getY(), 10,10);
//        gc.strokeRect(pos.getX(), pos.getY(), 10,10);
        gc.drawImage(
                isMoving ? runScorpion[frame]:idleScorpion[frame], pos.getX()-25, pos.getY()-25, 50, 50);
    }

    public boolean isAlive = true;

    @Override
    public void run() {
        //Tercer plano
        while (isAlive) {
            isMoving = true;
            frame = (frame+1)%6;
            double deltaX = Math.random() * 6 - 3;
            double deltaY = Math.random() * 6 - 3;
            pos.setY(pos.getY() + deltaY);
            pos.setX(pos.getX() + deltaX);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
