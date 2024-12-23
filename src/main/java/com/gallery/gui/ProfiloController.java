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

    @FXML
    private WebView webView;

    private User currentUser;

    private final AlertInfo alert = new AlertInfo();

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String htmlFilePath = getClass().getResource("/com/gallery/gui/profilo/profilo.html").toExternalForm();
        webEngine.load(htmlFilePath);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Profilo HTML caricato con successo");
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javafxController", this);
                if (currentUser != null) {
                    updateHTML(currentUser);
                }
            }
        });
    }

    // Metodo per impostare i dati dell'utente
    public void setUserData(User user) {
        this.currentUser = user;
        if (webView.getEngine().getLoadWorker().getState() == Worker.State.SUCCEEDED) {
            updateHTML(user);
        }
        updateJavaFXLabels(user);
    }

    // Metodo per aggiornare i dati HTML
    private void updateHTML(User user) {
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

    // Metodo per aggiornare le etichette JavaFX
    private void updateJavaFXLabels(User user) {
        if (usernameLabel != null) usernameLabel.setText(user.getUsername());
        if (emailLabel != null) emailLabel.setText(user.getEmail());
        if (roleLabel != null) roleLabel.setText("Utente");
    }

    public void updateProfileData(String usernameValue, String emailValue, String passwordValue) {
        System.out.println("updateProfileData chiamato con: " + usernameValue + ", " + emailValue + ", " + passwordValue);
        if (usernameValue == null || usernameValue.isEmpty() ||
                emailValue == null || emailValue.isEmpty() ||
                passwordValue == null || passwordValue.isEmpty()) {
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
        WebEngine webEngine = webView.getEngine();
        try {
            webEngine.executeScript("document.getElementById('username').innerText = '" + usernameValue + "';");
            webEngine.executeScript("document.getElementById('email').innerText = '" + emailValue + "';");
            webEngine.executeScript("document.getElementById('password').innerText = '" + passwordValue + "';");
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento dei dati nella WebView: " + e.getMessage());
        }
        alert.showAlertInfo("Successo", "Credenziali aggiornate correttamente");
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
    public void logout() {
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

    @FXML
    public void exitApp() {
        try {
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.close();
            System.out.println("Applicazione chiusa con successo.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore durante la chiusura dell'applicazione.");
        }
    }
}
