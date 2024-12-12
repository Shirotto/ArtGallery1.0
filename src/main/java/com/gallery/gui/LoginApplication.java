package com.gallery.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Carica il file FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);  // Imposta la scena
        stage.setTitle("Button with Stars Effect");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
