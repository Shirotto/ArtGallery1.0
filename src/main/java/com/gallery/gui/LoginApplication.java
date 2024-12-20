package com.gallery.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) {
        showLogin(stage);
    }

    public static void showLogin(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.setFill(javafx.scene.paint.Color.WHITE);

            stage.setTitle("");
            stage.setScene(scene);
            stage.setWidth(570);
            stage.setHeight(580);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il caricamento della schermata di login.");
        }
    }
    public static void main(String[] args) {
        launch();
    }
}