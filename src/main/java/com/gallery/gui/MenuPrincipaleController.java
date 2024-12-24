package com.gallery.gui;

import com.entity.User;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class MenuPrincipaleController {

    private AlertInfo alert = new AlertInfo();

    @FXML
    private WebView webView;

    private ProfiloController profilo = new ProfiloController();

    @FXML
    private Text welcomeText;

    private User currentUser;


    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String htmlFilePath = getClass().getResource("/com/gallery/gui/menuprincipale/menu.html").toExternalForm();
        if (htmlFilePath == null) {
            System.err.println("File HTML non trovato.");
            return;
        }
        webEngine.load(htmlFilePath);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javafxController", this);
            }
            if (currentUser != null) {
                profilo.updateHTML(currentUser, webView);
            }
        });
    }

    @FXML
    public void exitApp() {
        System.exit(0);
    }

    @FXML
    public void logout() {
        profilo.logout(webView);
    }

    @FXML
    public void updateProfilo(String nuovoUsername, String nuovaEmail, String nuovaPassword) {
        if (profilo == null) {
            System.err.println("ProfiloController non è inizializzato!");
            return;
        }
        if (nuovoUsername == null || nuovaEmail == null || nuovaPassword == null ||
                nuovoUsername.isEmpty() || nuovaEmail.isEmpty() || nuovaPassword.isEmpty()) {
            System.out.println("Tutti i campi devono essere compilati!");
            return;
        }
        profilo.updateProfileData(nuovoUsername, nuovaEmail, nuovaPassword, webView);
        System.out.println("Dati aggiornati con successo!");
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (profilo != null) {
            profilo.setUserData(user, webView);
        }
    }
}
