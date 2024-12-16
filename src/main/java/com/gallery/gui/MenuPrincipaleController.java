package com.gallery.gui;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuPrincipaleController {


    private ProfiloApplication profilo = new ProfiloApplication();

    @FXML
    private Text welcomeText;

    @FXML
    private void goToProfile() {
       profilo.openProfile();
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void exitApp() {
        // Chiudi l'applicazione
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        stage.close();
    }


}
