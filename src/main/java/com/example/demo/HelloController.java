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
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

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
    private ArrayList<Double> enemyXCoordinates;
    private ArrayList<Double> enemyYCoordinates;

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
        enemyXCoordinates = new ArrayList<>(Arrays.asList(700.0, 150.0, 100.0, 750.0));
        enemyYCoordinates = new ArrayList<>(Arrays.asList(100.0, 700.0, 150.0, 150.0));
        avatar = new Avatar();
        new Thread(avatar).start(); // Esto ejecuta el código dentro de run() en paralelo
        portal = new Portal();
        new Thread(portal).start();
        levels = new ArrayList<>();

        //Generar el primer mapa
        Level l1 = new Level(0);
        l1.generateMap(canvas, l1.getId());
        l1.setColor(Color.WHITE);
        int randomIndex = new Random().nextInt(enemyXCoordinates.size());
        Enemy e1lv1 = new ChasingEnemy(new Vector(enemyXCoordinates.get(randomIndex), enemyYCoordinates.get(randomIndex)));
        Enemy e2lv1 = new BidirectionalHorizontalEnemy(new Vector(525, 300));
        new Thread(e1lv1).start();
        new Thread(e2lv1).start();
        l1.getEnemies().add(e1lv1);
        l1.getEnemies().add(e2lv1);
        l1.setArm(new Arm(0));
        levels.add(l1);

        //Generar el segundo mapa
        Level l2 = new Level(1);
        l2.generateMap(canvas, l2.getId());
        l2.setColor(Color.GRAY);
        int randomIndex2 = new Random().nextInt(enemyXCoordinates.size());
        Enemy e1Lv2 = new ChasingEnemy(new Vector(enemyXCoordinates.get(randomIndex2), enemyYCoordinates.get(randomIndex2)));
        Enemy e2Lv2 = new BidirectionalVerticalEnemy(new Vector(100, 700));
        Enemy e3Lv2 = new BidirectionalHorizontalEnemy(new Vector(1000, 80));
        Enemy e4Lv2 = new BidirectionalHorizontalEnemy(new Vector(1000, 700));
        new Thread(e1Lv2).start();
        new Thread(e2Lv2).start();
        new Thread(e3Lv2).start();
        new Thread(e4Lv2).start();
        l2.getEnemies().add(e1Lv2);
        l2.getEnemies().add(e2Lv2);
        l2.getEnemies().add(e3Lv2);
        l2.getEnemies().add(e4Lv2);
        l2.setArm(new Arm(1));
        levels.add(l2);

        //Generar el tercer mapa
        Level l3 = new Level(2);
        l3.generateMap(canvas, l3.getId());
        l3.setColor(Color.WHITE);
        Enemy e1Lv3 = new ChasingEnemy(new Vector(700, 100));
        Enemy e2lv3 = new ChasingEnemy(new Vector(150, 700));
        Enemy e3lv3 = new ChasingEnemy(new Vector(100, 150));
        Enemy e4lv3 = new ChasingEnemy(new Vector(750, 150));
//        int randomIndex3 = new Random().nextInt(enemyXCoordinates.size());
//        for (int i = 0; i<= 4; i++) {
//
//        }
        new Thread(e1Lv3).start();
        new Thread(e2lv3).start();
        new Thread(e3lv3).start();
        new Thread(e4lv3).start();
        l3.getEnemies().add(e1Lv3);
        l3.getEnemies().add(e2lv3);
        l3.getEnemies().add(e3lv3);
        l3.getEnemies().add(e4lv3);
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
                Arm arm = level.getArm(); // Obtener las armas del nivel
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
                    // Dibujar el portal solo si no quedan enemigos en el nivel
                    if (checkEnemyCount() && currentLevel != 2) {
                        portal.draw(gc);
                    }

                    for (int i = 0; i < level.getWalls().size(); i++) {
                        level.getWalls().get(i).draw();
                    }

                    if (avatar.getArm() != null) {
                        for (int i = 0; i < avatar.getArm().getBullets().size(); i++) {
                            avatar.getArm().getBullets().get(i).draw(gc);
                            if (isOutside(avatar.getArm().getBullets().get(i).pos.getX(), avatar.getArm().getBullets().get(i).pos.getY())) {
                                avatar.getArm().getBullets().remove(i);
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < level.getEnemies().size(); i++) {
                        level.getEnemies().get(i).draw(gc);
                    }

                    // Si el jugador ha perdido todas sus vidas se muestra pantalla de game over
                    if (avatar.getLives() <= 0) {
                        isAlive = false;
                        Stage stage = (Stage) canvas.getScene().getWindow();
                        stage.close();
                        HelloApplication.openWindow("gameOver-view.fxml");
                    }

                    if (currentLevel == 2 && checkEnemyCount()) {
                        isAlive = false;
                        Stage stage = (Stage) canvas.getScene().getWindow();
                        stage.close();
                        HelloApplication.openWindow("win-view.fxml");
                    }

                });

                // Colisiones del jugador con el portal y los límites del mapa
                checkAvatarCollisionsWithLimitMapAndPortal();
                //Colisiones de balas con enemigos
                checkEnemyCollisionWithBullets();
                // Colisiones entre el avatar y el enemigo
                checkEnemyCollisionWithAvatar();
                // Colisiones de las balas con los muros
                checkWallCollisionsWithBullets();
                // Movimiento del jugador
                updateAvatarPosition();
                // Colisiones avatar con los muros
                checkAvatarWallCollisions();
                // Colisiones enemigo con los muros
                checkEnemyWallCollisions();

                for (int i = 0; i < level.getEnemies().size(); i++) {
                    double distanceX;
                    double distanceY;
                    Enemy enemy = level.getEnemies().get(i);
                    if (enemy instanceof ChasingEnemy) {
                        distanceX = enemy.pos.getX() - avatar.pos.getX();
                        distanceY = enemy.pos.getY() - avatar.pos.getY();
                        if (distanceX != 0) {
                            if (distanceX < 0) {
                                enemy.pos.setX(enemy.pos.getX() + 0.6);
                            } else {
                                enemy.pos.setX(enemy.pos.getX() - 0.6);
                            }
                        }
                        if (distanceY != 0) {
                            if (distanceY < 0) {
                                enemy.pos.setY(enemy.pos.getY() + 0.6);
                            } else {
                                enemy.pos.setY(enemy.pos.getY() - 0.6);
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ae.start();
    }

    public void checkAvatarCollisionsWithLimitMapAndPortal() {
        // Limite izquierdo del mapa
        if (avatar.pos.getX() < 25) {
            avatar.pos.setX(25);
        }
        // Limite derecho del mapa
        if (avatar.pos.getX() > canvas.getWidth() - 25) {
            avatar.pos.setX(canvas.getWidth() - 25);
        }
        // Limite inferior del mapa
        if (avatar.pos.getY() > canvas.getHeight() - 25) {
            avatar.pos.setY(canvas.getHeight() - 25);
        }
        // Limite superior del mapa
        if (avatar.pos.getY() < 25) {
            avatar.pos.setY(25);
        }
        if (currentLevel != 2) {
            // Colisión con el portal
            BoundingBox avatarBoundingBox = (BoundingBox) avatar.getBoundsInParent();
            BoundingBox portalBoundingBox = new BoundingBox(
                    portal.getX(), portal.getY(), 80, 80);
            if (avatarBoundingBox.intersects(portalBoundingBox) && checkEnemyCount()) {
                currentLevel++;
            }
        }
    }

    public void checkEnemyCollisionWithBullets() {
        Level level = levels.get(currentLevel);

        if (avatar.getArm() != null) {
            for (int i = 0; i < avatar.getArm().getBullets().size(); i++) {
                Bullet bullet = avatar.getArm().getBullets().get(i);
                for (int j = 0; j < level.getEnemies().size(); j++) {
                    Enemy enemy = level.getEnemies().get(j);

                    BoundingBox bulletBoundingBox = new BoundingBox(
                            bullet.pos.getX(), bullet.pos.getY(), 10, 10);
                    BoundingBox enemyBoundingBox = new BoundingBox(
                            enemy.pos.getX(), enemy.pos.getY(), 50, 50);

                    if (bulletBoundingBox.intersects(enemyBoundingBox)) {
                        avatar.getArm().getBullets().remove(i);
                        level.getEnemies().get(j).setLives(level.getEnemies().get(j).getLives() - 1);
                        if (level.getEnemies().get(j).getLives() == 0) level.getEnemies().remove(j);
                        break;
                    }
                }
            }
        }
    }

    public void checkEnemyCollisionWithAvatar() {
        Level level = levels.get(currentLevel);

        for (int j = 0; j < level.getEnemies().size(); j++) {
            Enemy enemy = level.getEnemies().get(j);

            BoundingBox avatarBoundingBox = (BoundingBox) avatar.getBoundsInParent();

            BoundingBox enemyBoundingBox = new BoundingBox(
                    enemy.pos.getX(), enemy.pos.getY(), 50, 50);

            if (avatarBoundingBox.intersects(enemyBoundingBox)) {
                if (!avatar.isInvincible() && avatar.getLives() > 0) {
                    avatar.setInvincible(true);
                    avatar.setLives(avatar.getLives() - 1);
                    // Agregar un retraso antes de volver a ser vulnerable
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        avatar.setInvincible(false);
                    }).start();
                }
                break;
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


    public void checkEnemyWallCollisions() {
        Level level = levels.get(currentLevel);

        // Verificar colisiones con los muros
        for (Wall wall : level.getWalls()) {
            for (Enemy enemy : level.getEnemies()) {
                if (enemy.isColliding(wall)) {
                    // Obtener la superposición en el eje X
                    double overlapX = enemy.getOverlapX(wall);
                    // Obtener la superposición en el eje Y
                    double overlapY = enemy.getOverlapY(wall);

                    if (overlapX < overlapY) {
                        // Colisión en el eje X
                        if (enemy.pos.getX() > wall.getX()) {
                            // Colisión en el lado derecho de la pared
                            enemy.pos.setX(wall.getX() + wall.getRectangle().getWidth() + 25);
                        } else if (enemy.pos.getX() < wall.getX()) {
                            // Colisión en el lado izquierdo de la pared
                            enemy.pos.setX(wall.getX() - 25);
                        }
                    } else {
                        // Colisión en el eje Y
                        if (enemy.pos.getY() > wall.getY()) {
                            // Colisión en la parte inferior de la pared y.
                            enemy.pos.setY(wall.getY() + wall.getRectangle().getHeight() + 25);
                        } else if (enemy.pos.getY() < wall.getY()) {
                            // Colisión en la parte superior de la pared
                            enemy.pos.setY(wall.getY() - 25);
                        }
                    }
                }
            }
        }
    }

    public void checkAvatarWallCollisions() {
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

    private boolean checkEnemyCount() {
        Level level = levels.get(currentLevel);
        return level.getEnemies().isEmpty();
    }


    public boolean isOutside(double x, double y) {
        return x < -10 || y < -10 || x > canvas.getWidth() || y > canvas.getHeight();
    }

}