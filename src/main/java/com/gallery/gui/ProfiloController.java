package com.gallery.gui;

import com.GestioneDB.GestioneOpere;
import com.entity.Opera;
import com.entity.User;
import com.util.HibernateUtil;
import com.util.PasswordUtil;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Base64;
import java.util.List;

public class ProfiloController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    private User currentUser;

    private final AlertInfo alert = new AlertInfo();

    // Metodo per impostare i dati dell'utente
    public void setUserData(User user, WebView webView) {
        this.currentUser = user;
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                updateHTML(user, webView);
            }
        });
    }

    // Metodo per aggiornare i dati HTML
    public void updateHTML(User user,WebView webView) {
        WebEngine webEngine = webView.getEngine();
        try {
            webEngine.executeScript("document.getElementById('username').innerText = '" + user.getUsername() + "';");
            webEngine.executeScript("document.getElementById('email').innerText = '" + user.getEmail() + "';");
            webEngine.executeScript("document.getElementById('password').innerText = '" + user.getPassword() + "';");
            webEngine.executeScript("document.getElementById('role').innerText = 'Utente';");
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento dei dati nella WebView: " + e.getMessage());
        }
    }

    public void updateProfileData(String usernameValue, String emailValue, String passwordValue, WebView webView) {
        System.out.println("updateProfileData chiamato con: " + usernameValue + ", " + emailValue + ", " + passwordValue);

        // Controllo che i campi non siano vuoti
        if (usernameValue == null || usernameValue.trim().isEmpty() ||
                emailValue == null || emailValue.trim().isEmpty() ||
                passwordValue == null || passwordValue.trim().isEmpty()) {
            alert.showAlertInfo("Errore", "Tutti i campi devono essere compilati.");
            return;
        }

        // Verifica della validità dell'email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!emailValue.matches(emailRegex)) {
            alert.showAlertInfo("Errore", "Inserire un indirizzo email valido.");
            return;
        }

        // Verifica che il nuovo nome utente sia diverso dal precedente
        if (usernameValue.equals(currentUser.getUsername())) {
            alert.showAlertInfo("Errore", "Il nuovo nome utente deve essere diverso dal precedente.");
            return;
        }

        // Verifica che la nuova password sia diversa dalla precedente
        if (PasswordUtil.checkPassword(passwordValue, currentUser.getPassword())) {
            alert.showAlertInfo("Errore", "La nuova password deve essere diversa dalla precedente.");
            return;
        }

        // Verifica della validità della password (deve avere almeno 8 caratteri, una lettera maiuscola, un numero e un carattere speciale)
        String passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        if (!passwordValue.matches(passwordRegex)) {
            alert.showAlertInfo("Errore", "La password deve contenere almeno 8 caratteri, " +
                    "una lettera maiuscola, un numero e un carattere speciale.");
            return;
        }

        // Cripta la nuova password prima di salvarla
        String hashedPassword = PasswordUtil.hashPassword(passwordValue);

        // Aggiorna i dati dell'utente
        currentUser.setUsername(usernameValue);
        currentUser.setEmail(emailValue);
        currentUser.setPassword(hashedPassword);  // Imposta la password criptata

        // Salva i dati aggiornati nel database
        saveUserDataToDatabase(currentUser);

        // Aggiorna la vista HTML
        WebEngine webEngine = webView.getEngine();
        updateHTML(currentUser, webView);

        alert.showAlertInfo("Successo", "Credenziali aggiornate correttamente");
    }

    public void mostraOpereUtente(User currentUser, WebView webView) {
        List<Opera> opere = GestioneOpere.getOpereByUser(currentUser);
        System.out.println("Numero di opere caricate dall'utente: " + (opere != null ? opere.size() : 0));
        StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("const opereCaricateRow = document.getElementById('opere-caricate-row');")
                .append("if (opereCaricateRow) { opereCaricateRow.innerHTML = ''; }")
                .append("else { console.error('Elemento opere-caricate-row non trovato.'); }");
        for (Opera opera : opere) {
            String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(opera.getImmagine());
            String descrizione = opera.getDescrizione().replace("'", "\\'");
            String nome = opera.getNome().replace("'", "\\'");
            String autore = opera.getAutore().replace("'", "\\'");
            String tecnica = opera.getTecnica().replace("'", "\\'");
            String dimensione = opera.getDimensione() != null ? opera.getDimensione().replace("'", "\\'") : "N/A";
            int anno = opera.getAnno();

            scriptBuilder.append("opereCaricateRow.innerHTML += `")
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


    private void saveUserDataToDatabase(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            System.out.println("Dati utente aggiornati nel database: " + user);
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
    }

    @FXML
    public void logout(WebView webView) {
        try {
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.close();
            alert.showAlertInfo("Logout", "Logout avvenuto con successo");
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
}
