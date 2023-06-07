package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Enemy extends Drawing implements Runnable {
    private int lives;

    private ArrayList<Bullet> bullets;
    public Enemy(Vector pos) {
        this.pos = pos;
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    @Override
    public void run() {
    }

    public boolean isColliding(Wall wall) { return false; }

    public double getOverlapX(Wall wall) {
        return 0;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public double getOverlapY(Wall wall) {
        return 0;
    }
}
