package com.gallery.gui;

import com.entity.User;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuPrincipaleController {

    private AlertInfo alert = new AlertInfo();


    private ProfiloApplication profilo = new ProfiloApplication();

    @FXML
    private Text welcomeText;

    private User currentUser;


    @FXML
    private void exitApp() {
        // Chiudi l'applicazione
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void goToProfile() {
        if (currentUser != null) {
            profilo.openProfile(currentUser); // Passa l'utente al profilo
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.close();
        } else {

            alert.showAlertErrore("Errore", "Nessun utente loggato.");
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
    }


}
