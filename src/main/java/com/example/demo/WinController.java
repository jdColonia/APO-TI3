package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WinController {

    @FXML
    private Button waitingBtn;

    @FXML
    public void win(ActionEvent event) {
        Stage stage = (Stage) this.waitingBtn.getScene().getWindow();
        stage.close();
        HelloApplication.openWindow("exitScreen-view.fxml");
    }

}
