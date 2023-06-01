package com.example.demo;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    @FXML
    private Canvas canvas;
    @FXML
    private ImageView playerBullets;

    @FXML
    private Label playerLbl;

    @FXML
    private ImageView playerLives;
    private Image background;
    private GraphicsContext gc;
    private ArrayList<Level> levels;
    private int currentLevel = 0;
    private Image[] heartsImage;
    private Image[] bulletsImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseMoved(this::onMouseMoved);
        loadHeartsImage();
        loadBulletsImage();
        avatar = new Avatar();
        new Thread(avatar).start(); // Esto ejecuta el código dentro de run() en paralelo
        levels = new ArrayList<>();

        //Generar el primer mapa
        Level l1 = new Level(0);
        l1.generateMap(canvas, l1.getId());
        l1.setColor(Color.WHITE);
        Enemy e = new Enemy(new Vector(500, 200), TypeEnemy.SCORPION);
        new Thread(e).start();
        l1.getEnemies().add(e);
        l1.getEnemies().add(new Enemy(new Vector(500, 300), TypeEnemy.SCORPION));
        levels.add(l1);

        //Generar el segundo mapa
        Level l2 = new Level(1);
        l2.generateMap(canvas, l2.getId());
        l2.setColor(Color.GRAY);
        l2.getEnemies().add(new Enemy(new Vector(100, 100), TypeEnemy.SCORPION));
        l2.getEnemies().add(new Enemy(new Vector(100, 700), TypeEnemy.SCORPION));
        Enemy eLv2 = new Enemy(new Vector(1000, 80), TypeEnemy.SCORPION);
        new Thread(eLv2).start();
        l2.getEnemies().add(eLv2);
        l2.getEnemies().add(new Enemy(new Vector(1000, 700), TypeEnemy.SCORPION));
        levels.add(l2);

        //Generar el tercer mapa
        Level l3 = new Level(2);
        l3.generateMap(canvas, l3.getId());
        l3.setColor(Color.WHITE);
        Enemy eLv3 = new Enemy(new Vector(700, 100), TypeEnemy.SCORPION);
        new Thread(eLv3).start();
        l3.getEnemies().add(eLv3);
        l3.getEnemies().add(new Enemy(new Vector(100, 700), TypeEnemy.SCORPION));
        levels.add(l3);

        draw();
    }

    private void onMouseMoved(MouseEvent e) {
        System.out.println("moviendo");
        double relativePosition = e.getX() - avatar.pos.getX();
        avatar.setFacingRight(
                relativePosition > 0
        );
    }

    private void onMousePressed(MouseEvent e) {
        System.out.println("X: " +e.getX() + "Y: "+e.getY());

        double diffX = e.getX() - avatar.pos.getX()-25;
        double diffY = e.getY() - avatar.pos.getY()-25;
        Vector diff = new Vector(diffX, diffY);
        diff.normalize();
        diff.setMag(4);


        levels.get(currentLevel).getBullets().add(
                new Bullet(
                        new Vector(avatar.pos.getX()+25, avatar.pos.getY()+25),
                        diff
                )
        );
    }


    private boolean isAlive = true;

    private boolean Apressed = false;
    private boolean Wpressed = false;
    private boolean Spressed = false;
    private boolean Dpressed = false;


    private Avatar avatar;


    public void onKeyReleased(KeyEvent event){
        switch (event.getCode()){
            case W: Wpressed = false; break;
            case A: Apressed = false; break;
            case S: Spressed = false; break;
            case D: Dpressed = false; break;
        }
    }
    public void onKeyPressed(KeyEvent event){
        System.out.println(event.getCode());
        switch (event.getCode()){
            case W: Wpressed = true; break;
            case A: Apressed = true; break;
            case S: Spressed = true; break;
            case D: Dpressed = true; break;
        }
    }


    public void draw(){
        //

        drawBullets();
        drawLives();
        Thread ae = new Thread(()->{
            while(isAlive){
                //Dibujar en el lienzo
                Level level = levels.get(currentLevel);
                Platform.runLater(()->{//Runnable
                    //Lo que hagamos aqui, corre en el main thread
                    gc.drawImage(level.getBackground(), 0, 0, canvas.getWidth(), canvas.getHeight());
                    gc.fillRect(canvas.getWidth(), 0, 10, 10);
                    avatar.setMoving(Wpressed || Spressed || Dpressed || Apressed);
                    avatar.draw(gc);

                    for (int i = 0; i < level.getWalls().size(); i++) {
                        level.getWalls().get(i).draw();;
                    }

                    for(int i=0 ; i<level.getBullets().size() ; i++){
                        level.getBullets().get(i).draw(gc);
                        if(isOutside(level.getBullets().get(i).pos.getX(), level.getBullets().get(i).pos.getY())){
                            level.getBullets().remove(i);
                        }
                    }

                    for(int i=0 ; i<level.getEnemies().size() ; i++){
                        level.getEnemies().get(i).draw(gc);
                    }
                });

                //Calculos geometricos

                //Paredes
                if(avatar.pos.getX() < 25) {
                    avatar.pos.setX(25);
                }
                if(avatar.pos.getY() > canvas.getHeight() - 25) {
                    avatar.pos.setY(canvas.getHeight() - 25);
                }
                if(avatar.pos.getY() < 0) {
                    switch (currentLevel) {
                        case 0 -> currentLevel = 1;
                        case 1 -> currentLevel = 2;
                        case 2 -> currentLevel = 0;
                    }
                    avatar.pos.setY(canvas.getHeight());
                }


                //Colisiones
                for (int i = 0; i < level.getBullets().size(); i++) {
                    Bullet bn = level.getBullets().get(i);
                    for (int j = 0; j < level.getEnemies().size(); j++) {
                        Enemy en = level.getEnemies().get(j);

                        double distance = Math.sqrt(
                                Math.pow(en.pos.getX() - bn.pos.getX(), 2) +
                                        Math.pow(en.pos.getY() - bn.pos.getY(), 2)
                        );

                        if (distance < 5) {
                            level.getBullets().remove(i);
                            level.getEnemies().remove(j);
                        }
                    }
                }

                if(Wpressed){
                    avatar.pos.setY(avatar.pos.getY()-0.8);
                }
                if (Apressed) {
                    avatar.pos.setX(avatar.pos.getX()-0.8);
                }
                if (Spressed) {
                    avatar.pos.setY(avatar.pos.getY()+0.8);
                }
                if (Dpressed) {
                    avatar.pos.setX(avatar.pos.getX()+0.8);
                }


                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        });
        ae.start();
    }

        public void loadHeartsImage() {
        heartsImage = new Image[6];
        for (int i = 1; i <= 5 ; i++) {
            String uri = "file:" + HelloApplication.class.getResource("heartLabel/"+i+"hearts.png").getPath();
            heartsImage[i-1] = new Image(uri);
        }
        String uri = "file:" + HelloApplication.class.getResource("heartLabel/skull-0lives.png").getPath();
        heartsImage[5] = new Image(uri);
    }

    public void drawLives() {
        new Thread(() -> {
            while(isAlive){
                if(avatar.getLives() == 5){
                    playerLives.setImage(heartsImage[4]);
                } else if(avatar.getLives() ==4){
                    playerLives.setImage(heartsImage[3]);
                } else if(avatar.getLives() ==3){
                    playerLives.setImage(heartsImage[2]);
                } else if(avatar.getLives() ==2){
                    playerLives.setImage(heartsImage[1]);
                } else if(avatar.getLives() ==1){
                    playerLives.setImage(heartsImage[0]);
                } else {
                    playerLives.setImage(heartsImage[5]);
                }
            }

            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void loadBulletsImage() {
        bulletsImage = new Image[6];
        for (int i = 0; i <= 5 ; i++) {
            String uri = "file:" + HelloApplication.class.getResource("bulletLabel/"+i+"bullet.png").getPath();
            bulletsImage[i] = new Image(uri);
        }
    }

    public void drawBullets(){
        new Thread(() -> {
            while(isAlive){
                if(avatar.getAmmo() ==5){
                    playerBullets.setImage(bulletsImage[5]);
                } else if(avatar.getAmmo() ==4){
                    playerBullets.setImage(bulletsImage[4]);
                } else if(avatar.getAmmo() ==3){
                    playerBullets.setImage(bulletsImage[3]);
                } else if(avatar.getAmmo() ==2){
                    playerBullets.setImage(bulletsImage[2]);
                } else if(avatar.getAmmo() ==1){
                    playerBullets.setImage(bulletsImage[1]);
                } else{
                    playerBullets.setImage(bulletsImage[0]);
                }
            }

            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }).start();
    }

    public boolean isOutside(double x, double y) {
        return x<-10 || y<-10 || x>canvas.getWidth() || y>canvas.getHeight();
    }


}






