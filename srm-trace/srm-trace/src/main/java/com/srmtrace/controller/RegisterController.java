package com.srmtrace.controller;

import com.srmtrace.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {
    @FXML public TextField username;
    @FXML public PasswordField password;
    @FXML public TextField displayName;
    @FXML public Button submit;
    @FXML public Label status;

    @FXML
    public void onSubmit() {
        boolean ok = AuthService.register(username.getText().trim(), password.getText().trim(), displayName.getText().trim());
        if (ok) {
            status.setText("Registered. Close and login.");
        } else status.setText("Registration failed (duplicate?).");
    }

    @FXML
    public void onClose() {
        ((Stage) submit.getScene().getWindow()).close();
    }
}
