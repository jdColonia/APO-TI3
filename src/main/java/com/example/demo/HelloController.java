package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
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

    @FXML
    private ImageView playerArm;

    private Image background;
    private GraphicsContext gc;
    private ArrayList<Level> levels;
    public int currentLevel = 0;
    private Image[] heartsImage;
    private Image[] bulletsImage;
    private Avatar avatar;
    private Arm currentArm;
    private Portal portal;

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
        portal = new Portal();
        new Thread(portal).start();
        levels = new ArrayList<>();

        //Generar el primer mapa
        Level l1 = new Level(0);
        l1.generateMap(canvas, l1.getId());
        l1.setColor(Color.WHITE);
        Enemy e = new Enemy(new Vector(500, 200), TypeEnemy.SCORPION);
        new Thread(e).start();
        l1.getEnemies().add(e);
        l1.getEnemies().add(new Enemy(new Vector(500, 300), TypeEnemy.SCORPION));
        l1.setArm(new Arm(0));
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
        l2.setArm(new Arm(1));
        levels.add(l2);

        //Generar el tercer mapa
        Level l3 = new Level(2);
        l3.generateMap(canvas, l3.getId());
        l3.setColor(Color.WHITE);
        Enemy eLv3 = new Enemy(new Vector(700, 100), TypeEnemy.SCORPION);
        new Thread(eLv3).start();
        l3.getEnemies().add(eLv3);
        l3.getEnemies().add(new Enemy(new Vector(100, 700), TypeEnemy.SCORPION));
        l3.setArm(new Arm(2));
        levels.add(l3);

        draw();
    }

    private void onMouseMoved(MouseEvent e) {
        double relativePosition = e.getX() - avatar.pos.getX();
        avatar.setFacingRight(
                relativePosition > 0
        );
    }

    private void onMousePressed(MouseEvent e) {
        double diffX = e.getX() - avatar.pos.getX() - 25;
        double diffY = e.getY() - avatar.pos.getY() - 25;
        Vector diff = new Vector(diffX, diffY);
        diff.normalize();
        diff.setMag(4);

        if (avatar.getArm() != null && avatar.getArm().getAmmo() > 0) {
            avatar.getArm().shoot(new Vector(avatar.pos.getX() + 25, avatar.pos.getY() + 25), diff);
        }
    }

    private boolean isAlive = true;

    private boolean Apressed = false;
    private boolean Wpressed = false;
    private boolean Spressed = false;
    private boolean Dpressed = false;

    public void onKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case W -> Wpressed = false;
            case A -> Apressed = false;
            case S -> Spressed = false;
            case D -> Dpressed = false;
        }
    }

    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W -> Wpressed = true;
            case A -> Apressed = true;
            case S -> Spressed = true;
            case D -> Dpressed = true;
            // Recargar el arma
            case R -> {
                if (avatar.getArm() != null) avatar.getArm().reload();
            }
            case E -> {
                int current = currentLevel;
                Level level = levels.get(current); // Obtener el nivel actual
                Arm arm = level.getArm(); // Obtener el armas del nivel
                double distance = Math.sqrt(Math.pow(avatar.pos.getX() - arm.getX(), 2) +
                        Math.pow(avatar.pos.getY() - arm.getY(), 2));
                if (distance < 50) {
                    if (currentArm != null) {
                        currentArm.setCollected(false);
                        level.setArm(currentArm);
                    }
                    currentArm = arm;
                    avatar.setArm(arm);
                    arm.setCollected(true);
                    drawBullets();
                }
            }
        }
    }

    public void draw() {
        //
        drawLives();
        Thread ae = new Thread(() -> {
            while (isAlive) {
                //Dibujar en el lienzo
                Level level = levels.get(currentLevel);
                Platform.runLater(() -> {//Runnable
                    //Lo que hagamos aqui, corre en el main thread
                    gc.drawImage(level.getBackground(), 0, 0, canvas.getWidth(), canvas.getHeight());
                    gc.fillRect(canvas.getWidth(), 0, 10, 10);
                    avatar.setMoving(Wpressed || Spressed || Dpressed || Apressed);
                    avatar.draw(gc);
                    // Dibujar el arma
                    drawArm();
                    // Dibujar el portal
                    portal.draw(gc);

                    for (int i = 0; i < level.getWalls().size(); i++) {
                        level.getWalls().get(i).draw();
                    }

                    if (avatar.getArm() != null) {
                        for (int i = 0; i < avatar.getArm().getBullets().size(); i++) {
                            avatar.getArm().getBullets().get(i).draw(gc);
                            if (isOutside(avatar.getArm().getBullets().get(i).pos.getX(), avatar.getArm().getBullets().get(i).pos.getY())) {
                                avatar.getArm().getBullets().remove(i);
                            }
                        }
                    }

                    for (int i = 0; i < level.getEnemies().size(); i++) {
                        level.getEnemies().get(i).draw(gc);
                    }
                });

                //Calculos geometricos

                //Paredes
                if (avatar.pos.getX() < 25) {
                    avatar.pos.setX(25);
                }
                if (avatar.pos.getY() > canvas.getHeight() - 25) {
                    avatar.pos.setY(canvas.getHeight() - 25);
                }
                if (avatar.pos.getY() < 0) {
                    switch (currentLevel) {
                        case 0 -> currentLevel = 1;
                        case 1 -> currentLevel = 2;
                        case 2 -> currentLevel = 0;
                    }
                    avatar.pos.setY(canvas.getHeight());
                }

                //Colisiones de balas con enemigos
                if (avatar.getArm() != null) {
                    for (int i = 0; i < avatar.getArm().getBullets().size(); i++) {
                        Bullet bn = avatar.getArm().getBullets().get(i);
                        for (int j = 0; j < level.getEnemies().size(); j++) {
                            Enemy en = level.getEnemies().get(j);

                            double distance = Math.sqrt(
                                    Math.pow(en.pos.getX() - bn.pos.getX(), 2) +
                                            Math.pow(en.pos.getY() - bn.pos.getY(), 2)
                            );

                            if (distance < 5) {
                                avatar.getArm().getBullets().remove(i);
                                level.getEnemies().remove(j);
                            }
                        }
                    }
                }
                // Colisiones de las balas con los muros
                checkWallCollisionsWithBullets();
                // Movimiento del jugador
                updateAvatarPosition();
                // Colisiones con los muros
                checkWallCollisions();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ae.start();
    }

    public void chec2kWallCollisionsWithBullets() {
        Level level = levels.get(currentLevel);

        // Verificar colisiones con los muros
        if (avatar.getArm() != null) {
            for (int i = 0; i < avatar.getArm().getBullets().size(); i++) {
                Bullet bullet = avatar.getArm().getBullets().get(i);
                for (int j = 0; j < level.getWalls().size(); j++) {
                    Wall wall = level.getWalls().get(j);

                    double distance = Math.sqrt(
                            Math.pow(wall.getX() - bullet.pos.getX(), 2) +
                                    Math.pow(wall.getY() - bullet.pos.getY(), 2)
                    );

                    if (distance < 25) {
                        avatar.getArm().getBullets().remove(i);
                        level.getWalls().get(j).setDamage(1);
                        if (level.getWalls().get(j).getDamage() <= 0) level.getWalls().remove(j);
                        break;
                    }
                }
            }
        }
    }

    public void checkWallCollisionsWithBullets() {
        Level level = levels.get(currentLevel);

        // Verificar colisiones con los muros
        if (avatar.getArm() != null) {
            ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
            for (Bullet bullet : avatar.getArm().getBullets()) {
                BoundingBox bulletBoundingBox = new BoundingBox(
                        bullet.pos.getX(), bullet.pos.getY(), 10, 10);

                for (Wall wall : level.getWalls()) {
                    BoundingBox wallBoundingBox = new BoundingBox(
                            wall.getX(), wall.getY(),
                            wall.getRectangle().getWidth(), wall.getRectangle().getHeight());

                    if (bulletBoundingBox.intersects(wallBoundingBox)) {
                        bulletsToRemove.add(bullet);
                        wall.setDamage(1);
                        if (wall.getDamage() <= 0) level.getWalls().remove(wall);
                        break; // Salir del bucle de las paredes
                    }
                }
            }

            avatar.getArm().getBullets().removeAll(bulletsToRemove);
        }
    }


    public void checkWallCollisions() {
        Level level = levels.get(currentLevel);

        // Verificar colisiones con los muros
        for (Wall wall : level.getWalls()) {
            if (avatar.isColliding(wall)) {
                // Obtener la superposición en el eje X
                double overlapX = avatar.getOverlapX(wall);
                // Obtener la superposición en el eje Y
                double overlapY = avatar.getOverlapY(wall);

                if (overlapX < overlapY) {
                    // Colisión en el eje X
                    if (Apressed && !Dpressed && avatar.pos.getX() > wall.getX()) {
                        // Colisión en el lado derecho de la pared
                        avatar.pos.setX(wall.getX() + wall.getRectangle().getWidth() + 25);
                    } else if (Dpressed && !Apressed && avatar.pos.getX() < wall.getX()) {
                        // Colisión en el lado izquierdo de la pared
                        avatar.pos.setX(wall.getX() - 25);
                    }
                } else {
                    // Colisión en el eje Y
                    if (Wpressed && !Spressed && avatar.pos.getY() > wall.getY()) {
                        // Colisión en la parte inferior de la pared y.
                        avatar.pos.setY(wall.getY() + wall.getRectangle().getHeight() + 25);
                    } else if (Spressed && !Wpressed && avatar.pos.getY() < wall.getY()) {
                        // Colisión en la parte superior de la pared
                        avatar.pos.setY(wall.getY() - 25);
                    }
                }
            }
        }
    }

    public void updateAvatarPosition() {
        double speed = 1;
        if (Wpressed) {
            avatar.pos.setY(avatar.pos.getY() - speed);
        }
        if (Apressed) {
            avatar.pos.setX(avatar.pos.getX() - speed);
        }
        if (Spressed) {
            avatar.pos.setY(avatar.pos.getY() + speed);
        }
        if (Dpressed) {
            avatar.pos.setX(avatar.pos.getX() + speed);
        }
    }


    public void loadHeartsImage() {
        heartsImage = new Image[6];
        for (int i = 1; i <= 5; i++) {
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("heartLabel/" + i + "hearts.png")).getPath();
            heartsImage[i - 1] = new Image(uri);
        }
        String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("heartLabel/skull-0lives.png")).getPath();
        heartsImage[5] = new Image(uri);
    }

    public void drawLives() {
        new Thread(() -> {
            while (isAlive) {
                Platform.runLater(() -> {
                    int lives = avatar.getLives();
                    if (lives >= 1 && lives <= 5) {
                        playerLives.setImage(heartsImage[lives - 1]);
                    } else {
                        playerLives.setImage(heartsImage[5]); // Imagen de la calavera para vidas incorrectas
                    }
                });
                try {
                    Thread.sleep(20); // Esperar un tiempo antes de actualizar las vidas
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void loadBulletsImage() {
        bulletsImage = new Image[6];
        for (int i = 0; i <= 5; i++) {
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("bulletLabel/" + i + "bullet.png")).getPath();
            bulletsImage[i] = new Image(uri);
        }
    }

    public void drawBullets() {
        new Thread(() -> {
            while (isAlive) {
                int ammo = avatar.getArm().getAmmo();
                Platform.runLater(() -> {
                    playerBullets.setImage(bulletsImage[ammo]);
                });
                try {
                    Thread.sleep(100); // Esperar un tiempo antes de actualizar las balas
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void drawArm() {
        Arm currentArm = getCurrentArm(currentLevel);
        if (!currentArm.isCollected()) currentArm.draw(gc);
        new Thread(() -> {
            while (isAlive) {
                if (avatar.getArm() != null && avatar.getArm().isCollected())
                    playerArm.setImage(avatar.getArm().getArmImage());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Arm getCurrentArm(int currentLevelID) {
        return levels.get(currentLevelID).getArm();
    }

    public boolean isOutside(double x, double y) {
        return x < -10 || y < -10 || x > canvas.getWidth() || y > canvas.getHeight();
    }

}