package com.gallery.gui;

import com.GestioneDB.GestioneUtente;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;


public class LoginController {

    @FXML
    private WebView webView;
    @FXML
    private StackPane stackPane;

    private MenuPrincipaleApplication menuPrincipaleApplication = new MenuPrincipaleApplication();

    private GestioneUtente gestione = new GestioneUtente(); // Inizializza GestioneUtente


    @FXML
    public void initialize() {
        // Carica il contenuto HTML nel WebView
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String htmlFilePath = getClass().getResource("login/login.html").toExternalForm();
        webEngine.load(htmlFilePath);

        // listener per verificare se la pagina è stata caricata correttamente
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Pagina caricata con successo");
                JSObject jsObject = (JSObject) webEngine.executeScript("window");
                jsObject.setMember("javafxController", this);
                System.out.println("Controller JavaFX impostato correttamente");
            }
        });
        JSObject jsObject = (JSObject) webEngine.executeScript("window");
        jsObject.setMember("javafxController", this);
    }

    // Metodo per gestire il login
    public void handleLoginButtonClick(String email, String password) {
        // Controlla se l'email e la password sono valide
        if (!ControllaEmail(email) || !ControllaPassword(password)) {
            return; // Se i dati non sono validi, interrompi l'esecuzione
        }

        // Verifica le credenziali nel database
        boolean credenzialiValide = gestione.verificaCredenziali(email, password);

        if (credenzialiValide) {
            // Se le credenziali sono corrette, chiudi la finestra di login
            closeCurrentWindow();

            // Mostra il menu principale
            System.out.println("Login avvenuto con successo!");
            showAlert("", "Login avvenuto con successo!");
            menuPrincipaleApplication.openMainMenu();  // Usa la tua logica per aprire il menu
        } else {
            // Se le credenziali non sono valide, mostra un messaggio di errore
            showAlert("Errore", "Email o password errate.");
        }
    }

    // Metodo per gestire la registrazione
    public void handleSignUpButtonClick(String name, String email, String password) {


        // Controlla se l'email è valida
        if (!ControllaEmail(email) || !ControllaPassword(password)) {
            return; // Se i dati non sono validi, interrompi l'esecuzione
        }

        // Verifica se l'email è già registrata nel database
        boolean registrazioneSuccesso = gestione.verificaERegistraUtente(name, email, password);

        if (registrazioneSuccesso) {
            // Se la registrazione è avvenuta con successo
            showAlert("Successo", "Registrazione avvenuta con successo!");


            closeCurrentWindow();
            System.out.println("Registrazione avvenuta con successo!");
            riapriFinestraLogin();


        } else {
            //Altrimenti
            showAlert("Errore", "Controlla se sei registrato, o Utilizza un altro Nome");
        }

    }

    public boolean ControllaEmail(String email) {
        if (email.isEmpty() || !email.contains("@gmail.com") && !email.contains("@outlook.it")) {
            showAlert("Errore", "Inserisci un'email valida.");
            return false;
        }
        return true;
    }

    public boolean ControllaPassword(String password) {

        if (password.isEmpty()) {
            showAlert("Errore", "Inserisci una password.");
            return false;
        } else {

            if (password.length() < 8) {
                showAlert("Errore", "La password deve avere almeno 8 caratteri");
                return false;
            }
            // Verifica se la password contiene almeno un numero
            if (!password.matches(".*\\d.*")) { // Verifica se c'è almeno un numero
                showAlert("Errore", "La password deve contenere almeno un numero.");
                return false;
            }
        }
        return true;
    }

    // Metodo per chiudere la finestra attuale
    private void closeCurrentWindow() {
        Stage currentStage = (Stage) stackPane.getScene().getWindow();
        currentStage.close();
    }

    // Metodo per mostrare alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
    private void riapriFinestraLogin() {
        try {

            // Carica il file FXML della finestra di login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
            Stage stage = new Stage(); // Crea una nuova finestra
            stage.setTitle("Login");
            stage.setScene(new Scene(loader.load())); // Carica la scena
            stage.setWidth(570);
            stage.setHeight(580);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore", "Non è stato possibile aprire la finestra di login.");
        }
    }

}






