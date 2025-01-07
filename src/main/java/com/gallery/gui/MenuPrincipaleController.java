package com.gallery.gui;

import com.GestioneDB.GestioneOpere;
import com.entity.Opera;
import com.entity.User;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.*;

public class MenuPrincipaleController {

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
                aggiornaGalleria();// fa in modo che le opere restino salvate alla riapertura
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
        if (currentUser == null) {
            System.err.println("Errore: Utente corrente è null!");
            return;
        }
        System.out.println("Utente impostato: " + currentUser.getUsername());
        if (profilo != null) {
            profilo.setUserData(currentUser, webView);
            profilo.mostraOpereUtente(currentUser, webView);
        }
    }


    public void aggiornaGalleria() {
        List<Opera> opere = GestioneOpere.getAllOpere();
        StringBuilder script = new StringBuilder("const galleryRow = document.getElementById('row-cat1');");
        script.append("galleryRow.innerHTML = '';");
        for (Opera opera : opere) {
            String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(opera.getImmagine());

            script.append("galleryRow.innerHTML += `")
                    .append("<div class='gallery-item' data-description='")
                    .append(opera.getDescrizione())
                    .append("'><img src='")
                    .append(base64Image)
                    .append("' alt='")
                    .append(opera.getNome())
                    .append("'></div>`;");
        }
        webView.getEngine().executeScript(script.toString());
    }

    public void salvaOpera(String titolo, String autore, int anno, String tecnica, String descrizione, String imageDataBase64,String dimensione) {
        byte[] immagine = java.util.Base64.getDecoder().decode(imageDataBase64.split(",")[1]);
        GestioneOpere.salvaOperaDb(titolo, autore, anno, tecnica, currentUser, descrizione, immagine,dimensione);
        webView.getEngine().reload();
        aggiornaGalleria();
    }

    public void showProfileSection() {
        webView.getEngine().executeScript("showSection('profile-section')");
        profilo.mostraOpereUtente(currentUser, webView);
    }

    public void getOpereCaricate() {
        profilo.mostraOpereUtente(currentUser,webView);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Messaggio");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
