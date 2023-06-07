package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class Portal extends Drawing implements Runnable {

    private int x = 1000, y = 675;
    private int frame = 0;
    private Image[] portalImages;

    String uri1 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("portal/portal1.png")).getPath();
    String uri2 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("portal/portal2.png")).getPath();
    String uri3 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("portal/portal3.png")).getPath();
    String uri4 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("portal/portal4.png")).getPath();
    String uri5 = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("portal/portal5.png")).getPath();

    public Portal() {
        // Cargar las imágenes del portal
        portalImages = new Image[5];
        portalImages[0] = new Image(uri1);
        portalImages[1] = new Image(uri2);
        portalImages[2] = new Image(uri3);
        portalImages[3] = new Image(uri4);
        portalImages[4] = new Image(uri5);
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Dibujar las tres imágenes del portal en un bucle
        gc.drawImage(frame >= 0 && frame <= 4 ? portalImages[frame] : portalImages[0], x, y, 80, 80);
    }

    public boolean isAlive = true;

    @Override
    public void run() {
        while (isAlive) {
            frame = (frame + 1) % 4;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
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
