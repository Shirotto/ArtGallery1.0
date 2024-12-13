package com.gallery.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuPrincipaleController {
    @FXML
    private Text welcomeText;
    @FXML
    private void goToProfile() {
        // Logica per andare al profilo
        showAlert("Navigazione", "Navigando al profilo...");
    }

    @FXML
    private void exitApp() {
        // Chiudi l'applicazione
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        stage.close();
    }

    // Metodo per mostrare alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
