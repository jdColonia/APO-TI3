package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Bullet extends Drawing {

    private final Vector dir;
    private final Image bulletImage;
    private Color color;

    public Bullet(Vector pos, Vector dir) {
        this.pos = pos;
        this.dir = dir;
        String uri = "file:" + HelloApplication.class.getResource("bulletLabel/circleBullet.png").getPath();
        bulletImage = new Image(uri);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);

        double radius = 5; // Radio del círculo
        double centerX = pos.getX(); // Coordenada X del centro del círculo
        double centerY = pos.getY(); // Coordenada Y del centro del círculo

        // Calcular las coordenadas X e Y desplazadas
        double offsetX = radius * Math.cos(pos.getX());
        double offsetY = radius * Math.sin(pos.getY());

        // Dibujar el círculo con las coordenadas desplazadas
        gc.fillOval(centerX - radius + offsetX, centerY - radius + offsetY, radius * 2, radius * 2);

        gc.drawImage(bulletImage, centerX - radius + offsetX, centerY - radius + offsetY, radius * 2, radius * 2);

        // Actualizar la posición del círculo
        pos.setX(pos.getX() + dir.getX());
        pos.setY(pos.getY() + dir.getY());
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
