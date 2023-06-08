package com.example.demo;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;

public class Level {

    private int id;
    private Color color;
    private Image background;
    private ArrayList<Enemy> enemies;
    private ArrayList<Wall> walls;
    private Arm arm;
    String uri1 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("background/BackgroundLv1.png")).getPath();
    String uri2 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("background/BackgroundLv2.png")).getPath();
    String uri3 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("background/BackgroundLv3.png")).getPath();

    public Level(int id) {
        this.id = id;
        switch (id) {
            case 0 -> background = new Image(uri1);
            case 1 -> background = new Image(uri2);
            case 2 -> background = new Image(uri3);
        }
        enemies = new ArrayList<>();
        walls = new ArrayList<>();
    }

    public void generateMap(Canvas canvas, int index) {
        walls = new ArrayList<>();

        for (int i = 350; i >= 200; i -= 50) { // Funciona
            Wall wall1 = new Wall(350, i, canvas, index);
            walls.add(wall1);
        }

        //horizontal
        for (int i = 400; i < 600; i += 50) { // Funciona
            Wall wall1 = new Wall(i, 100, canvas, index);
            walls.add(wall1);
        }

        //Pared vertical fondo arriba
        for (int i = 0; i < 250; i += 50) { // Funciona
            Wall wall1 = new Wall(800, i, canvas, index);
            walls.add(wall1);
        }

        //Pared vertical fondo abajo
        for (int i = 550; i <= canvas.getHeight(); i += 50) { // Funciona
            Wall wall1 = new Wall(750, i, canvas, index);
            walls.add(wall1);
        }

        //Pared vertical primera abajo
        for (int i = 550; i <= canvas.getHeight(); i += 50) { // Funciona
            Wall wall1 = new Wall(150, i, canvas, index);
            walls.add(wall1);
        }

        //Pared horizontal fondo arriba
        for (int i = 950; i <= 1000; i += 50) { // Funciona
            Wall wall1 = new Wall(i, 150, canvas, index);
            walls.add(wall1);
        }

        //Pared horizontal fondo abajo
        for (int i = 1000; i <= canvas.getWidth(); i += 50) { // Funciona
            Wall wall1 = new Wall(i, 500, canvas, index);
            walls.add(wall1);
        }

        //Pared horizontal medio abajo
        for (int i = 400; i <= 450; i += 50) { // Funciona
            Wall wall1 = new Wall(i, 500, canvas, index);
            walls.add(wall1);
        }

        //Pared vertical medio abajo
        for (int i = 450; i <= 450; i += 50) { // Funciona
            Wall wall1 = new Wall(300, i, canvas, index);
            walls.add(wall1);
        }

        for (int i = 200; i > 50; i -= 50) { // Funciona
            Wall wall1 = new Wall(i, 400, canvas, index);
            walls.add(wall1);
        }

        //modifica primera pared vertical
        for (int i = 0; i < 100; i += 50) { // Funciona
            Wall wall1 = new Wall(150, i, canvas, index);
            walls.add(wall1);
        }

        //Modifica primera pared vertical parte2
        for (int i = 200; i <= 250; i += 50) { // Funciona
            Wall wall1 = new Wall(150, i, canvas, index);
            walls.add(wall1);
        }

        for (int i = 200; i <= 400; i += 50) { // Funciona
            Wall wall1 = new Wall(650, i, canvas, index);
            walls.add(wall1);
        }

        for (int i = 55; i >= 550; i -= 50) { // Funciona
            Wall wall1 = new Wall(i, 350, canvas, index);
            walls.add(wall1);
        }

        for (int i = 750; i < 1000; i += 50) { // Funciona
            Wall wall1 = new Wall(i, 350, canvas, index);
            walls.add(wall1);
        }
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public void setWalls(ArrayList<Wall> walls) {
        this.walls = walls;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getBackground() {
        return background;
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public Arm getArm() {
        return arm;
    }

    public void setArm(Arm arm) {
        this.arm = arm;
    }

}
