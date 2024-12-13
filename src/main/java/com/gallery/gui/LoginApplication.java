package com.gallery.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Carica il file FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(javafx.scene.paint.Color.WHITE);
        stage.setTitle("Login Screen");
        stage.setScene(scene);

        // Limita le dimensioni della finestra
        stage.setWidth(570);     // Larghezza iniziale
        stage.setHeight(580);    // Altezza iniziale

        // Mostra la finestra
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}