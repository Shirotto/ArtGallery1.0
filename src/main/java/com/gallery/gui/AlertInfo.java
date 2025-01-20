package com.gallery.gui;

import javafx.scene.control.Alert;

public class AlertInfo {
    // Metodo per mostrare alert Errore
    public static void showAlertErrore(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Metodo per mostrare alert Info
    public static void showAlertInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

