package com.srmtrace;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.srmtrace.db.DB;
import java.io.File;

public class MainApp extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        // ensure data folder exists
        new File("data").mkdirs();
        DB.init(); // create DB and tables if missing

        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("SRM Trace");
        stage.setWidth(900);
        stage.setHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
