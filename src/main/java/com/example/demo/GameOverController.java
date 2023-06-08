package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameOverController {

    @FXML
    private Button waitingBtn;

    @FXML
    public void gameOver(ActionEvent event) {
        Stage stage = (Stage) this.waitingBtn.getScene().getWindow();
        stage.close();
        HelloApplication.openWindow("exitScreen-view.fxml");
    }

}