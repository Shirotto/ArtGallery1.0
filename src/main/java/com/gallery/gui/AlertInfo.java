package com.gallery.gui;

import javafx.scene.control.Alert;

public class AlertInfo {
    public static boolean DISABLE_ALERTS_FOR_TESTS = false;

    public static void showAlertErrore(String title, String message) {
        if (DISABLE_ALERTS_FOR_TESTS) {
            // In test, non aprire la finestra, magari stampa su console
            System.err.println("[TEST AlertErrore] " + title + " - " + message);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void showAlertInfo(String title, String message) {
        if (DISABLE_ALERTS_FOR_TESTS) {
            System.err.println("[TEST AlertInfo] " + title + " - " + message);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

