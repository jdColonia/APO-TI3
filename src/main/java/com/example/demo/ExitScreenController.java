package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ExitScreenController {

    @FXML
    private Button resetBtn;

    @FXML
    private Button exitBtn;

    @FXML
    public void resetGame(ActionEvent event) {
        Stage stage = (Stage) this.resetBtn.getScene().getWindow();
        stage.close();
        HelloApplication.openWindow("startingScreen-view.fxml");
    }

    @FXML
    public void exitGame(ActionEvent event) {
        Stage stage = (Stage) this.exitBtn.getScene().getWindow();
        stage.close();
    }

}
