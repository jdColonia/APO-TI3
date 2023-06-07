package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartingScreenController {

    @FXML
    private Button startBtn;

    @FXML
    public void startGame(ActionEvent event) {
        Stage stage = (Stage) this.startBtn.getScene().getWindow();
        stage.close();
        HelloApplication.openWindow("hello-view.fxml");
    }

}
