package com.example.demo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Wall {
    public int x,y;
    private Canvas canvas;
    private Image wall;
    public int damage = 5;
    private GraphicsContext gc;
    public Rectangle rectangle;
    String uri1 = "file:"+ Objects.requireNonNull(HelloApplication.class.getResource("background/BlockLv1.png")).getPath();
    String uri2 = "file:"+ Objects.requireNonNull(HelloApplication.class.getResource("background/BlockLv2.png")).getPath();
    String uri3 = "file:"+ Objects.requireNonNull(HelloApplication.class.getResource("background/BlockLv3.png")).getPath();

    public Wall(int x, int y, Canvas canvas, int index) {
        this.x = x;
        this.y = y;
        gc = canvas.getGraphicsContext2D();
        switch (index) {
            case 0 -> wall = new Image(uri1);
            case 1 -> wall = new Image(uri2);
            case 2 -> wall = new Image(uri3);
        }
        this.canvas = canvas;
    }

    public void draw(){
        gc.setFill(Color.GOLD);
        gc.fillRect(x,y,50,50);
        gc.drawImage(wall,x,y,50,50);
        rectangle = new Rectangle(x,y,50,50);
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

}
