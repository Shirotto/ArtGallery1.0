package com.gallery.gui;

import com.GestioneDB.GestioneUtente;
import com.entity.User;
import com.gallery.gui.AlertInfo;
import com.gallery.gui.Windows;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;


public class LoginController {

    @FXML
    private WebView webView;
    @FXML
    private StackPane stackPane;

    GestioneUtente gestioneUtente = new GestioneUtente("hibernate.cfg.xml");

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String htmlFilePath = getClass().getResource("login/login.html").toExternalForm();
        webEngine.load(htmlFilePath);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Pagina caricata con successo");
                JSObject jsObject = (JSObject) webEngine.executeScript("window");
                jsObject.setMember("ControllerLogin", this);
                System.out.println("Controller JavaFX impostato correttamente");
            }
        });
        JSObject jsObject = (JSObject) webEngine.executeScript("window");
        jsObject.setMember("ControllerLogin", this);
    }

    // Gestione evento di login
    public void handleLoginButtonClick(String email, String password) {
        if (gestioneUtente.verificaCredenzialiDaccesso(email, password)) {
            User currentUser = gestioneUtente.getUserByEmail(email);
            Windows.chiudiFinestraCorrente(stackPane);
            Windows.apriMenuPrincipale(currentUser);
        } else {
            AlertInfo.showAlertErrore("Errore", "Email o password non valide.");

        }
    }

    // Gestione evento registrazione
    public void handleSignUpButtonClick(String name, String email, String password) {
        if (gestioneUtente.registraUtente(name, email, password)) {
            Windows.chiudiFinestraCorrente(stackPane);
            Windows.apriFinestraLogin();
        }
    }


}










