package com.gallery.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class LoginController {

    @FXML
    private WebView webView;

    @FXML
    public void initialize() {
        // Carica il contenuto HTML nel WebView
        WebEngine webEngine = webView.getEngine();
        String htmlFilePath = getClass().getResource("login/login.html").toExternalForm();
        webEngine.load(htmlFilePath);
    }
}