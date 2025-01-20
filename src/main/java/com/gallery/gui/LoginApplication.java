package com.gallery.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) {
        showLogin(stage);
    }

    public static void showLogin(Stage stage) {
        Windows.apriFinestraLogin();
    }
    public static  void main(String[] args){launch();}

}