package com.gallery.gui;

import com.GestioneDB.GestioneOpere;
import com.entity.User;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        //mi fa visualizzare gli alert definiti nel html
        webEngine.setOnAlert(event -> {
            String message = event.getData();
            System.out.println("Alert JavaScript: " + message);
            // Oltre alla console, puoi visualizzare l'alert con una finestra JavaFX
           showAlert(message);
        });



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
        profilo.updateProfileData(nuovoUsername, nuovaEmail, nuovaPassword, webView);
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (profilo != null) {
            profilo.setUserData(user, webView);
        }
    }

    // Metodo per gestire il salvataggio dell'opera
    public void salvaOpera(String titolo, String autore, int anno, String tecnica, User currentUser, String descrizione, String imageDataBase64) {
        // Converte i dati base64 dell'immagine in byte[]
        byte[] immagine = java.util.Base64.getDecoder().decode(imageDataBase64.split(",")[1]);

        // Salva l'opera nel database
        GestioneOpere.salvaOpera(titolo, autore, anno, tecnica, currentUser, descrizione, immagine);
        System.out.println("Opera salvata con successo.");
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Messaggio");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
