package com.gallery.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class LoginController {

    @FXML
    private Label welcomeText;  // Riferimento al Label

    @FXML
    private WebView webView;  // Riferimento alla WebView

    @FXML
    public void initialize() {
        // Imposta il testo di benvenuto
        welcomeText.setText("Benvenuto! Accedi per continuare.");

        // Ottieni il WebEngine dalla WebView
        WebEngine webEngine = webView.getEngine();

        // Carica il file HTML
        webEngine.load(getClass().getResource("login/loginbutton.html").toExternalForm());

        // Aggiungi il JavaScript bridge
        webEngine.setJavaScriptEnabled(true);

        // Listener per esporre il JavaScript bridge
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                // Registra il controller come oggetto accessibile da JavaScript
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", this);  // Questo collega il controller al JavaScript
            }
        });
    }

    // Metodo invocato dal JavaScript
    public void handleButtonClick() {
        welcomeText.setText("Login button clicked!");
    }
}