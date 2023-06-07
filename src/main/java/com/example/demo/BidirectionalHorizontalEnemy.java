package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class BidirectionalHorizontalEnemy extends Enemy {
    private final Image[] idleScorpion;
    private final Image[] runScorpion;
    private int frame = 0;
    private boolean isMoving = false;
    private final double movementLimit = 100;  // Límite de movimiento en píxeles
    private int movementDirection = 1;
    private double initialX;
    private int lives;

    public BidirectionalHorizontalEnemy(Vector pos) {
        super(pos);
        lives = 3;
        initialX = pos.getX();
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

            // Calcula la distancia actual en X desde la posición inicial
            double distanceX = Math.abs(pos.getX() - initialX);

            // Cambia de dirección si se alcanza el límite de movimiento
            if (distanceX >= movementLimit) {
                movementDirection *= -1;  // Invierte la dirección de movimiento
            }

            // Actualiza la posición en el eje X según la dirección de movimiento
            pos.setX(pos.getX() + (movementDirection * 1));  // Ajusta la velocidad del movimiento según tus necesidades


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }
}
