package com.gallery.gui;

import com.GestioneDB.GestioneOpere;
import com.GestioneDB.GestioneUtente;
import com.entity.Opera;
import com.entity.User;
import com.util.PasswordUtil;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


import java.util.Base64;
import java.util.List;

public class Profilo {


    // Metodo per aggiornare i dati HTML
    public static void updateHTML(User user, WebView webView) {
        WebEngine webEngine = webView.getEngine();

        try {
            webEngine.executeScript("document.getElementById('username').innerText = '" + user.getUsername() + "';");
            webEngine.executeScript("document.getElementById('email').innerText = '" + user.getEmail() + "';");
            //webEngine.executeScript("document.getElementById('password').innerText = '" + user.getPassword() + "';");
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento dei dati nella WebView: " + e.getMessage());
        }
    }

    public static void updateProfileData(User currentUser,  String usernameValue, String emailValue, String passwordValue, WebView webView) {
        // controlliamo prima che non abbia inserito le vecchie credenziali e che le nuove siano accettabili
        System.out.println("updateProfileData chiamato con: " + usernameValue + ", " + emailValue + ", " + passwordValue);
        ValidazioneInput.validaInput(usernameValue,emailValue,passwordValue);
        if (usernameValue.equals(currentUser.getUsername())) {
            AlertInfo.showAlertInfo("Errore", "Il nuovo nome utente deve essere diverso dal precedente.");
            return;
        }
        if (emailValue.equals(currentUser.getEmail())) {
            AlertInfo.showAlertInfo("Errore", "La nuova email deve essere diversa dalla precedente.");
            return;
        }
        if (PasswordUtil.checkPassword(passwordValue, currentUser.getPassword())) {
            AlertInfo.showAlertInfo("Errore", "La nuova password deve essere diversa dalla precedente.");
            return;
        }
        if (usernameValue.isEmpty()) {usernameValue=currentUser.getUsername();}
        if (emailValue.isEmpty()) emailValue=currentUser.getEmail();
        if (passwordValue.isEmpty()) {
            System.out.println("La vecchia pass è" + currentUser.getPassword());
            passwordValue=currentUser.getPassword(); // getpass prende il valore hashato, se non si vuole modificare la pass non serve fare altro
        }else {passwordValue = PasswordUtil.hashPassword(passwordValue);} // se si vuole cambiare pass cisogna hashare la nuova pass
        if (!ValidazioneInput.validaInputConAlert(usernameValue,emailValue,passwordValue)) return;

        // Cripta la nuova password prima di salvarla
        //String hashedPassword = PasswordUtil.hashPassword(passwordValue);
        // Aggiorna i dati dell'utente
        currentUser.setUsername(usernameValue);
        currentUser.setEmail(emailValue);
        currentUser.setPassword(passwordValue);  // Imposta la password criptata

        // Salva i dati aggiornati nel database
        GestioneUtente.saveUserDataToDatabase(currentUser);

        // Aggiorna la vista HTML
        WebEngine webEngine = webView.getEngine();
        updateHTML(currentUser, webView);

        AlertInfo.showAlertInfo("Successo", "Credenziali aggiornate correttamente");

        }


    public static void mostraOpereUtente(User currentUser, WebView webView) {

        System.out.println("mostraOpereUtente chiamato");
        List<Opera> opere = GestioneOpere.getOpereByUser(currentUser);
        System.out.println("Numero di opere caricate dall'utente: " + (opere != null ? opere.size() : 0));
        StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("var opereCaricateRow = document.getElementById('opere-caricate-row');")
                .append("if (opereCaricateRow) {")
                .append("  opereCaricateRow.innerHTML = '';") // Reset content if it exists
                .append("} else {")
                .append("  console.error('Elemento opere-caricate-row non trovato nel DOM.');")
                .append("}");
// Aggiungi le opere caricate dinamicamente
        for (Opera opera : opere) {
            String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(opera.getImmagine());
            String descrizione = opera.getDescrizione().replace("'", "\\'");
            String nome = opera.getNome().replace("'", "\\'");
            String autore = opera.getAutore().replace("'", "\\'");
            String tecnica = opera.getTecnica().replace("'", "\\'");
            String dimensione = opera.getDimensione() != null ? opera.getDimensione().replace("'", "\\'") : "N/A";
            int id = opera.getId();
            int anno = opera.getAnno();


            scriptBuilder.append("if (opereCaricateRow) {")
                    .append("opereCaricateRow.innerHTML += `")
                    .append("<div class='gallery-item' data-id='") // Aggiungi l'ID come attributo data-id
                    .append(id)
                    .append("' data-description='") // Aggiungi uno spazio tra l'ID e il resto degli attributi
                    .append(descrizione)
                    .append("' data-author='")
                    .append(autore)
                    .append("' data-technique='")
                    .append(tecnica)
                    .append("' data-year='")
                    .append(anno)
                    .append("' data-dimension='")
                    .append(dimensione)
                    .append("' data-user='")
                    .append(currentUser.getUsername().replace("'", "\\'"))
                    .append("'>")
                    .append("<img src='")
                    .append(base64Image)
                    .append("' alt='")
                    .append(nome)
                    .append("'>")
                    .append("</div>`;")
                    .append("}");

        }
        scriptBuilder.append("attachGalleryItemListeners();");
        String script = scriptBuilder.toString();
        try {
            webView.getEngine().executeScript(script);

        } catch (Exception e) {
            System.err.println("Errore durante l'iniezione dello script nella WebView: " + e.getMessage());
        }


    }


    @FXML
    public static void logout(WebView webView) {
        try {
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.close();
            AlertInfo.showAlertInfo("Logout", "Logout avvenuto con successo");
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



    public static void assegnaOpera(String titolo, String autore, int anno, String tecnica, User currentUser, String descrizione, String imageDataBase64, String dimensione) {
        // Converte i dati base64 dell'immagine in byte[]
        byte[] immagine = java.util.Base64.getDecoder().decode(imageDataBase64.split(",")[1]);
        // Salva l'opera nel database
        GestioneOpere.salvaOperaNelDb(titolo, autore, anno, tecnica, currentUser, descrizione, immagine,dimensione);
        // Subito dopo il salvataggio, aggiorna la galleria senza ricaricare la pagina

    }





}
