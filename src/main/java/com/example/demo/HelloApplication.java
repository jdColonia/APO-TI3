package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        openWindow("startingScreen-view.fxml");
    }

    public static void openWindow(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Nuclear Throne");
            String uri = "file:" + Objects.requireNonNull(HelloApplication.class.getResource("cursor.png")).getPath();
            Cursor customCursor = Cursor.cursor(uri);
            scene.setCursor(customCursor);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}