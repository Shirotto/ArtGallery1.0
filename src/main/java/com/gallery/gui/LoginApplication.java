package com.gallery.gui;

import javafx.application.Application;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) {
        showLogin(stage);
    }

    public static void showLogin(Stage stage) {
        WindowsController.apriFinestraLogin();
    }
    public static void main(String[] args) {
        launch();
    }
}