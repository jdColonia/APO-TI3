package com.example.demo.control;

import com.example.demo.NuclearThronesApplication;
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
        NuclearThronesApplication.openWindow("exitScreen-view.fxml");
    }

}