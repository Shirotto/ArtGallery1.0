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
        Scene scene = new Scene(fxmlLoader.load());  // Non fissiamo la dimensione qui, lascia che il layout decida
        stage.setTitle("Login Screen");
        stage.setScene(scene);

        // Imposta la dimensione della finestra in base alla scena
        stage.setWidth(800);
        stage.setHeight(1000);

        // Mostra la finestra
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}