package com.example.demo;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
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

    // Verifica si el avatar está colisionando con el muro
    public boolean isColliding(Wall wall) {
        Bounds enemyBounds = getBoundsInParent();
        Bounds wallBounds = wall.getRectangle().getBoundsInParent();
        return enemyBounds.intersects(wallBounds);
    }

    // Calcula el solapamiento en el eje X entre el avatar y el muro
    public double getOverlapX(Wall wall) {
        Bounds enemyBounds = getBoundsInParent();
        Bounds wallBounds = wall.getRectangle().getBoundsInParent();
        // Calcula el solapamiento en el eje X
        if (enemyBounds.getMinX() < wallBounds.getMinX()) {
            // El avatar está a la izquierda del objeto
            return enemyBounds.getMaxX() - wallBounds.getMinX();
        } else {
            // El avatar está a la derecha del objeto
            return wallBounds.getMaxX() - enemyBounds.getMinX();
        }
    }

    // Calcula el solapamiento en el eje Y entre el avatar y el muro
    public double getOverlapY(Wall wall) {
        Bounds enemyBounds = getBoundsInParent();
        Bounds wallBounds = wall.getRectangle().getBoundsInParent();
        // Calcula el solapamiento en el eje Y
        if (enemyBounds.getMinY() < wallBounds.getMinY()) {
            // El avatar está arriba del objeto
            return enemyBounds.getMaxY() - wallBounds.getMinY();
        } else {
            // El avatar está abajo del objeto
            return wallBounds.getMaxY() - enemyBounds.getMinY();
        }
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
    public Bounds getBoundsInParent() {
        return new BoundingBox(pos.getX() - 25, pos.getY() - 25, 50, 50);
    }
}
