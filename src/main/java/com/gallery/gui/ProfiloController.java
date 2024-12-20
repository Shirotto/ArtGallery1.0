package com.gallery.gui;

import com.entity.User;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class ProfiloController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private WebView webView;

    private User currentUser;

    private final AlertInfo alert = new AlertInfo();

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String htmlFilePath = getClass().getResource("/com/gallery/gui/profilo/profilo.html").toExternalForm();
        webEngine.load(htmlFilePath);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Profilo HTML caricato con successo");
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javafxController", this);
                if (currentUser != null) {
                    updateHTML(currentUser);
                }
            }
        });
    }

    // Metodo per impostare i dati dell'utente
    public void setUserData(User user) {
        this.currentUser = user;
        if (webView.getEngine().getLoadWorker().getState() == Worker.State.SUCCEEDED) {
            updateHTML(user);
        }
        updateJavaFXLabels(user);
    }

    // Metodo per aggiornare i dati HTML
    private void updateHTML(User user) {
        WebEngine webEngine = webView.getEngine();
        try {
            webEngine.executeScript("document.getElementById('username').innerText = '" + user.getUsername() + "';");
            webEngine.executeScript("document.getElementById('email').innerText = '" + user.getEmail() + "';");
            webEngine.executeScript("document.getElementById('role').innerText = 'Utente';");
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento dei dati nella WebView: " + e.getMessage());
        }
    }

    // Metodo per aggiornare le etichette JavaFX
    private void updateJavaFXLabels(User user) {
        if (usernameLabel != null) usernameLabel.setText(user.getUsername());
        if (emailLabel != null) emailLabel.setText(user.getEmail());
        if (roleLabel != null) roleLabel.setText("Utente");
    }

    // Metodo per aggiornare campi specifici nel profilo
    public void updateProfileData(String field, String value) {
        switch (field.toLowerCase()) {
            case "username":
                currentUser.setUsername(value);
                break;
            case "email":
                currentUser.setEmail(value);
                break;
            case "password":
                currentUser.setPassword(value);
                break;
            default:
                System.err.println("Campo non riconosciuto: " + field);
        }
        WebEngine webEngine = webView.getEngine();
        try {
            webEngine.executeScript("document.getElementById('" + field.toLowerCase() + "').innerText = '" + value + "';");
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento dei dati nella WebView: " + e.getMessage());
        }
        saveUserDataToDatabase(currentUser);
    }

    private void saveUserDataToDatabase(User user) {
        // Qui implementare logica per cambio dati nel database
        System.out.println("Dati utente aggiornati nel database: " + user);
    }

    @FXML
    public void logout() {
        try {
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.close();
            alert.showAlertInfo("Logout", "Logout avvenuto con successo");
            Stage loginStage = new Stage();
            LoginApplication.showLogin(loginStage);
            loginStage.setWidth(570);
            loginStage.setHeight(580);
            loginStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore durante il logout.");
        }
    }

    @FXML
    public void exitApp() {
        try {
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.close();
            System.out.println("Applicazione chiusa con successo.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore durante la chiusura dell'applicazione.");
        }
    }
}
