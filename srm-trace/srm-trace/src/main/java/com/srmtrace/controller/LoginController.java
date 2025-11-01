package com.srmtrace.controller;

import com.srmtrace.MainApp;
import com.srmtrace.model.User;
import com.srmtrace.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class LoginController {
    @FXML public TextField username;
    @FXML public PasswordField password;
    @FXML public Button loginBtn;
    @FXML public Button regBtn;
    @FXML public Label status;

    @FXML
    public void initialize() {}

    @FXML
    public void onLogin() {
        String u = username.getText().trim();
        String p = password.getText().trim();
        User user = AuthService.login(u,p);
        if (user != null) {
            status.setText("Welcome " + (user.displayName==null?user.username:user.displayName));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                Scene scene = new Scene(loader.load());
                DashboardController dc = loader.getController();
                dc.setUser(user);
                Stage st = MainApp.primaryStage;
                st.setScene(scene);
            } catch (Exception ex) { ex.printStackTrace(); }
        } else {
            status.setText("Invalid credentials");
        }
    }

    @FXML
    public void onRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Register - SRM Trace");
            stage.setWidth(420); stage.setHeight(420);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
