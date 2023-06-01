package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Bullet extends Drawing {

    private final Vector dir;
    private final Image bulletImage;
    public Bullet(Vector pos, Vector dir){
        this.pos = pos;
        this.dir = dir;
        String uri = "file:"+ HelloApplication.class.getResource("bulletLabel/circleBullet.png").getPath();
        bulletImage = new Image(uri);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(pos.getX()-6, pos.getY()-6, 12,12);
        gc.drawImage(bulletImage,pos.getX()-6,pos.getY()-6,12,12);
        pos.setX( pos.getX() + dir.getX() );
        pos.setY( pos.getY() + dir.getY() );
    }
}
