package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class BidirectionalVerticalEnemy extends Enemy {
    private final Image[] idleScorpion;
    private final Image[] runScorpion;
    private int frame = 0;
    private boolean isMoving = false;
    private final double movementLimit = 100;  // Límite de movimiento en píxeles
    private int movementDirection = 1;
    private double initialY;
    private int lives;

    public BidirectionalVerticalEnemy(Vector pos) {
        super(pos);
        initialY = pos.getY();
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
        while (isAlive) {
            isMoving = true;
            frame = (frame + 1) % 6;

            // Calcula la distancia actual en Y desde la posición inicial
            double distanceY = Math.abs(pos.getY() - initialY);

            // Cambia de dirección si se alcanza el límite de movimiento
            if (distanceY >= movementLimit) {
                movementDirection *= -1;  // Invierte la dirección de movimiento
            }

            // Actualiza la posición en el eje Y según la dirección de movimiento
            pos.setY(pos.getY() + (movementDirection * 1));  // Ajusta la velocidad del movimiento según tus necesidades
            try {
                Thread.sleep(10);
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
