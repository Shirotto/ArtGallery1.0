package com.gallery.gui;

import com.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

public class ProfiloController {

    AlertInfo alert = new AlertInfo();

    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Text welcomeText;

    @FXML
    private WebView webView;

    private User currentUser;

    @FXML
    private void logout(ActionEvent event) {
        // Chiudi la finestra corrente
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
        alert.showAlertInfo("Logout", "Logout avvenuto con successo");

        try {
            Stage loginStage = new Stage();
            stage.setWidth(570);
            stage.setHeight(580);
            stage.setResizable(false);
            LoginApplication.showLogin(loginStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il caricamento della schermata di login.");
        }
    }

    // Metodo per impostare i dati dell'utente nel profilo
    public void setUserData(User user) {
        this.currentUser = user;

        // Aggiorna le etichette con i dati dell'utente
        if (currentUser != null) {
            usernameLabel.setText("Username: " + currentUser.getUsername());
            emailLabel.setText("email: " + currentUser.getEmail());
            roleLabel.setText("Role: " + "Utente");
        }
    }

    @FXML
    private void exitApp() {
        // Chiudi l'applicazione
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void initialize() {
        System.out.println("Inizializzazione WebView...");
        URL resourceUrl = getClass().getResource("/com/gallery/gui/profilo/profilo.html");
        if (resourceUrl == null) {
            System.err.println("File HTML non trovato: /com/gallery/gui/profilo/profilo.html");
        } else {
            String url = resourceUrl.toExternalForm();
            System.out.println("Caricamento HTML da: " + url);
            webView.getEngine().load(url);
        }
    }

}
