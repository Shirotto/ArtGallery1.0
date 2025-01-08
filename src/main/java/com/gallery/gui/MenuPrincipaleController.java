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

        profilo.setMenuPrincipaleController(this);


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
            profilo.mostraOpereUtente(currentUser, webView); // Aggiunge le opere caricate dall'utente
        }
    }



    public void aggiornaGalleria() {
        List<Opera> opere = GestioneOpere.getAllOpere();
        StringBuilder scriptBuilder = new StringBuilder("const galleryRow = document.getElementById('row-cat1');");

        scriptBuilder.append("galleryRow.innerHTML = '';");
        for (Opera opera : opere) {
            String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(opera.getImmagine());
            String descrizione = opera.getDescrizione().replace("'", "\\'");
            String nome = opera.getNome().replace("'", "\\'");
            String autore = opera.getAutore().replace("'", "\\'");
            String tecnica = opera.getTecnica().replace("'", "\\'");
            String dimensione = opera.getDimensione() != null ? opera.getDimensione().replace("'", "\\'") : "N/A";
            int anno = opera.getAnno();
            scriptBuilder.append("galleryRow.innerHTML += `")
                    .append("<div class='gallery-item' ")
                    .append("data-description='").append(descrizione).append("' ")
                    .append("data-author='").append(autore).append("' ")
                    .append("data-technique='").append(tecnica).append("' ")
                    .append("data-year='").append(anno).append("' ")
                    .append("data-dimension='").append(dimensione).append("' ")
                    .append("data-user='").append(currentUser.getUsername().replace("'", "\\'")).append("'>")
                    .append("<img src='").append(base64Image).append("' alt='").append(nome).append("'>")
                    .append("</div>`;");
        }
        scriptBuilder.append("attachGalleryItemListeners();");
        String script = scriptBuilder.toString();
        try {
            webView.getEngine().executeScript(script);
        } catch (Exception e) {
            System.err.println("Errore durante l'iniezione dello script nella WebView: " + e.getMessage());
        }
    }


    public void salvaOpera(String titolo, String autore, int anno, String tecnica, String descrizione, String imageDataBase64,String dimensione) {
        // Converte i dati base64 dell'immagine in byte[]
        byte[] immagine = java.util.Base64.getDecoder().decode(imageDataBase64.split(",")[1]);
        // Salva l'opera nel database
        GestioneOpere.salvaOperaDb(titolo, autore, anno, tecnica, currentUser, descrizione, immagine,dimensione);
        webView.getEngine().reload();
        // Subito dopo il salvataggio, aggiorna la galleria senza ricaricare la pagina
        aggiornaGalleria();
    }

    public void showProfileSection() {
        // 1) “mostra” la sezione nel WebView (chiamando la tua showSection('profile-section') lato JS)
         webView.getEngine().executeScript("showSection('profile-section')");

        // 2) e poi reinietti le opere in ‘opere-caricate-row’
        profilo.mostraOpereUtente(currentUser, webView);


    }

    public void profiloDopoEliminzione(){
        System.out.println("Profilo dopoEliminzione chiamatoooooooooooooooooo");

        profilo.mostraOpereUtente(currentUser, webView);

        aggiornaGalleria();

        webView.getEngine().reload();


    }




    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Messaggio");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
