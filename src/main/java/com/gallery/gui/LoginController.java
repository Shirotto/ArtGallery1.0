package com.gallery.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class LoginController {

    @FXML
    private Label welcomeText;  // Riferimento al Label

    @FXML
    private WebView webView;  // Riferimento alla WebView

    @FXML
    public void initialize() {
        // Imposta il testo di benvenuto
        welcomeText.setText("Welcome to the Starry Button Effect!");

        // Ottieni il WebEngine dalla WebView
        WebEngine webEngine = webView.getEngine();

        // Carica il file HTML dal classpath (index.html)
        webEngine.load(getClass().getResource("login/loginbutton.html").toExternalForm());
    }

    // Metodo per l'azione del bottone
    @FXML
    private void onHelloButtonClick() {
        welcomeText.setText("You clicked the button!");
    }
}
