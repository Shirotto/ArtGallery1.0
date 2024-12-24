package com.gallery.gui;

import com.entity.User;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class MenuPrincipaleController {

    private AlertInfo alert = new AlertInfo();

    @FXML
    private WebView webView;

    private ProfiloApplication profilo = new ProfiloApplication();

    @FXML
    private Text welcomeText;

    private User currentUser;

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String htmlFilePath = getClass().getResource("/com/gallery/gui/menuprincipale/menu.html").toExternalForm();
        webEngine.load(htmlFilePath);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javafxController", this);
            }
        });
    }

    @FXML
    public void exitApp() {
        try {
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("Errore durante la chiusura dell'applicazione: " + e.getMessage());
        }
    }

    @FXML
    public void openProfile() {
        if (currentUser != null) {
            profilo.openProfile(currentUser);
            try {
                Stage stage = (Stage) webView.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
                alert.showAlertErrore("Errore", "Impossibile chiudere il menu principale.");
            }
        } else {
            alert.showAlertErrore("Errore", "Nessun utente loggato.");
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
}
