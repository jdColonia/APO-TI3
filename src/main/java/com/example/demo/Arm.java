package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;

public class Arm extends Drawing {

    private int x = 150, y = 120;
    private Image arm;
    private ArrayList<Bullet> bullets;
    private int ammo; // Número de balas disponibles en el cargador
    private int maxAmmo; // Capacidad máxima del cargador
    private int index; // Nivel actual
    private boolean isCollected = false;

    String uri1 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("arm/arm-1.png")).getPath();
    String uri2 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("arm/arm-2.png")).getPath();
    String uri3 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("arm/arm-3.png")).getPath();

    public Arm(int index) {
        switch (index) {
            case 0 -> arm = new Image(uri1);
            case 1 -> arm = new Image(uri2);
            case 2 -> arm = new Image(uri3);
        }
        this.index = index;
        this.maxAmmo = 5;
        this.ammo = 5;
        bullets = new ArrayList<>();
    }

    public void shoot(Vector posPlayer, Vector diff) {
        Bullet bullet = new Bullet(posPlayer, diff);
        bullet.setColor(colorBullet()); // Establecer el color de la bala según el arma
        bullets.add(bullet);
        ammo--;
    }

    private Color colorBullet() {
        switch (index) {
            case 0 -> {
                return Color.GREEN;
            }
            case 1 -> {
                return Color.RED;
            }
            case 2 -> {
                return Color.BLACK;
            }
            default ->  {
                return Color.WHITE;
            }
        }
    }

    public void reload() {
        new Thread(() -> {
            while (ammo < maxAmmo) {
                ammo++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(arm, x, y, 25, 25);
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

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo -= ammo;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

}

