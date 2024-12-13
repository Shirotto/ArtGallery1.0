package com.gallery.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(javafx.scene.paint.Color.WHITE);
        stage.setTitle("Login Screen");
        stage.setScene(scene);
        stage.setWidth(570);
        stage.setHeight(580);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}