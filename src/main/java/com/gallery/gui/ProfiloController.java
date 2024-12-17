package com.gallery.gui;

import com.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    private User currentUser;

    @FXML
    private void logout(ActionEvent event) {
        // Chiudi la finestra corrente
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
        alert.showAlertInfo("Logout", "Logout avvenuto con successo");

        try {
            // Carica la schermata di login
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
            Scene loginScene = new Scene(fxmlLoader.load());

            // Crea un nuovo stage per la schermata di login
            Stage loginStage = new Stage();
            loginStage.setTitle("Login Screen");
            loginStage.setScene(loginScene);
            loginStage.setWidth(570);
            loginStage.setHeight(580);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il caricamento della schermata di login.");
        }
    }

    // Metodo per impostare i dati dell'utente nel profilo
    public void setUserData(User user){
        this.currentUser = user;

        // Aggiorna le etichette con i dati dell'utente
        if (currentUser != null) {
            usernameLabel.setText("Username: " + currentUser.getUsername());
            emailLabel.setText("email: " + currentUser.getEmail());
            roleLabel.setText("Role: " + "Strunz");
        }
    }

    @FXML
    private void exitApp() {
        // Chiudi l'applicazione
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        stage.close();
    }

}


