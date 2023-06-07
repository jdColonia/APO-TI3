package com.example.demo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Wall {

    private int x, y;
    private int damage = 3;

    private Canvas canvas;
    private Image wallImage;
    private GraphicsContext gc;
    private Rectangle rectangle;

    String uri1 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("background/BlockLv1.png")).getPath();
    String uri2 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("background/BlockLv2.png")).getPath();
    String uri3 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("background/BlockLv3.png")).getPath();

    public Wall(int x, int y, Canvas canvas, int index) {
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        rectangle = new Rectangle(x, y, 50, 50);
        switch (index) {
            case 0 -> wallImage = new Image(uri1);
            case 1 -> wallImage = new Image(uri2);
            case 2 -> wallImage = new Image(uri3);
        }
    }

    public void draw() {
        gc.setFill(Color.GOLD);
        gc.fillRect(x, y, 50, 50);
        gc.drawImage(wallImage, x, y, 50, 50);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage -= damage;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

}
