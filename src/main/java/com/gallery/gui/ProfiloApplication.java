package com.gallery.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfiloApplication {

    // Metodo per aprire il profilo in una nuova finestra
    public void openProfile() {
        try {
            Stage menuStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profilo/profilo-view.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            menuStage.setTitle("Profilo");
            menuStage.setScene(scene);
            menuStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore", "Impossibile aprire il profilo.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
