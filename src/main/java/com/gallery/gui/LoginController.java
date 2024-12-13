package com.gallery.gui;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;


public class LoginController {

    @FXML
    private WebView webView;
    @FXML
    private StackPane stackPane;
    private MenuPrincipaleApplication menuPrincipaleApplication = new MenuPrincipaleApplication();

    @FXML
    public void initialize() {
        // Carica il contenuto HTML nel WebView
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true); // Abilita JavaScript nel WebView
        String htmlFilePath = getClass().getResource("login/login.html").toExternalForm();
        webEngine.load(htmlFilePath);

        // Aggiungi un listener per verificare se la pagina è stata caricata correttamente
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
        //metodi per controllo email e password
        if (!ControllaEmail(email) || !ControllaPassword(password)) {
            return; // Se la validazione fallisce, interrompi l'esecuzione
        }
        //system out per vedere se escono i dati
        System.out.println("Hai cliccato il pulsante di login");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        // Se la validazione ha successo, chiudi la finestra di login e apri il menu principale
        closeCurrentWindow();
        menuPrincipaleApplication.openMainMenu();
    }

    // Metodo per gestire la registrazione
    public void handleSignUpButtonClick(String email, String password) {
        System.out.println("Hai cliccato il pulsante di registrazione");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        // Qui puoi aggiungere la logica per la registrazione, come la creazione di un nuovo utente
    }

    public boolean ControllaEmail(String email){
        if (email.isEmpty() || !email.contains("@") || !email.contains("ciao")) {
            showAlert("Errore", "Inserisci un'email valida.");
            return false;
        }
        return true;
    }

    public boolean ControllaPassword(String password) {
        if (password.isEmpty()) {
            showAlert("Errore", "Inserisci una password.");
            return false;
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
}
