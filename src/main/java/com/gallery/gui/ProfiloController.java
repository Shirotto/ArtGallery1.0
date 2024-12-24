package com.gallery.gui;

import com.entity.User;
import com.util.HibernateUtil;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        if (webView == null || webView.getEngine() == null) {
            System.err.println("WebView non inizializzata correttamente.");
            return;
        }
        System.out.println("Aggiornamento profilo: " + usernameValue + ", " + emailValue + ", " + passwordValue);
        if (usernameValue == null || emailValue == null || passwordValue == null ||
                usernameValue.isEmpty() || emailValue.isEmpty() || passwordValue.isEmpty()) {
            alert.showAlertInfo("Errore", "Tutti i campi devono essere compilati.");
            return;
        }
        if (usernameValue.equals(currentUser.getUsername())) {
            alert.showAlertInfo("Errore", "Il nuovo nome utente deve essere diverso dal precedente.");
            return;
        }
        if (passwordValue.equals(currentUser.getPassword())) {
            alert.showAlertInfo("Errore", "La nuova password deve essere diversa dalla precedente.");
            return;
        }
        String passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        if (!passwordValue.matches(passwordRegex)) {
            alert.showAlertInfo("Errore", "La password deve contenere almeno 8 caratteri, una lettera maiuscola, un numero e un carattere speciale.");
            return;
        }
        currentUser.setUsername(usernameValue);
        currentUser.setEmail(emailValue);
        currentUser.setPassword(passwordValue);
        saveUserDataToDatabase(currentUser);
        try {
            WebEngine webEngine = webView.getEngine();
            webEngine.executeScript("document.getElementById('username').innerText = '" + usernameValue + "';");
            webEngine.executeScript("document.getElementById('email').innerText = '" + emailValue + "';");
            webEngine.executeScript("document.getElementById('password').innerText = '" + passwordValue + "';");
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento dei dati nella WebView: " + e.getMessage());
        }
        alert.showAlertInfo("Successo", "Credenziali aggiornate correttamente.");
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
