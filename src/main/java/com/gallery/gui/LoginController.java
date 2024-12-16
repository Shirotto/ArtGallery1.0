package com.gallery.gui;
import com.GestioneDB.GestioneUtente;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import java.io.IOException;


public class LoginController {

    @FXML
    private WebView webView;
    @FXML
    private StackPane stackPane;

    private MenuPrincipaleApplication menuPrincipaleApplication = new MenuPrincipaleApplication();

    private GestioneUtente gestione = new GestioneUtente(); // Inizializza GestioneUtente


    @FXML
    public void initialize() {
        // Carica il contenuto HTML nel WebView
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String htmlFilePath = getClass().getResource("login/login.html").toExternalForm();
        webEngine.load(htmlFilePath);

        // listener per verificare se la pagina è stata caricata correttamente
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Pagina caricata con successo");
                JSObject jsObject = (JSObject) webEngine.executeScript("window");
                jsObject.setMember("javafxController", this);
                System.out.println("Controller JavaFX impostato correttamente");
            }
        });
        JSObject jsObject = (JSObject) webEngine.executeScript("window");
        jsObject.setMember("javafxController", this);
    }

    // Metodo per gestire il login
    public void handleLoginButtonClick(String email, String password) {
        // Controlla se l'email e la password sono valide
        if (!ControllaEmail(email) || !ControllaPassword(password)) {
            return; // Se i dati non sono validi, interrompi l'esecuzione
        }

        // Verifica le credenziali nel database
        boolean credenzialiValide = gestione.verificaCredenziali(email, password);

        if (credenzialiValide) {
            // Se le credenziali sono corrette, chiudi la finestra di login
            closeCurrentWindow();

            // Mostra il menu principale
            System.out.println("Login avvenuto con successo!");
            showAlertInfo("", "Login avvenuto con successo!");
            menuPrincipaleApplication.openMainMenu();
        } else {
            // Se le credenziali non sono valide, mostra un messaggio di errore
            showAlertErrore("Errore", "Email o password errate.");
        }
    }

    // Metodo per gestire la registrazione
    public void handleSignUpButtonClick(String name, String email, String password) {


        // Controlla se l'email è valida
        if (!ControllaEmail(email) || !ControllaPassword(password)) {
            return; // Se i dati non sono validi, interrompi l'esecuzione
        }

        // Verifica se l'email è già registrata nel database
        boolean registrazioneSuccesso = gestione.verificaERegistraUtente(name, email, password);

        if (registrazioneSuccesso) {
            // Se la registrazione è avvenuta con successo
            showAlertInfo("Successo", "Registrazione avvenuta con successo!");
            closeCurrentWindow();
            System.out.println("Registrazione avvenuta con successo!");
            //chiamata al metodo che riapre la finestra di login
            riapriFinestraLogin();
        } else {
            //altrimenti
            showAlertErrore("Errore", "Controlla se sei registrato, o Utilizza un altro Nome");
        }
    }

    private void riapriFinestraLogin() {
        try {

            // Riapre la finestra login dopo essersi registrati
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
            Stage stage = new Stage(); // Crea una nuova finestra
            stage.setTitle("Login");
            stage.setScene(new Scene(loader.load())); // Carica la scena
            stage.setWidth(570);
            stage.setHeight(580);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlertErrore("Errore", "Non è stato possibile aprire la finestra di login.");
        }
    }

    // Metodo per mostrare alert Errore
    private void showAlertErrore(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    // Metodo per mostrare alert Info
    private void showAlertInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    public boolean ControllaEmail(String email) {
        // Definisce il pattern per un'email valida
        String emailRegex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

        // Verifica se l'email è vuota o non corrisponde al pattern
        if (email.isEmpty() || !email.matches(emailRegex)) {
            showAlertErrore("Errore", "Inserisci un'email valida.");
            return false;
        }

        // Email valida
        return true;
    }

    public boolean ControllaPassword(String password) {
        // Verifica se la password è vuota
        if (password == null || password.isEmpty()) {
            showAlertErrore("Errore", "Inserisci una password.");
            return false;
        }

        // Verifica se la password contiene almeno 8 caratteri
        if (password.length() < 8) {
            showAlertErrore("Errore", "La password deve avere almeno 8 caratteri.");
            return false;
        }

        // Verifica se la password contiene almeno un numero
        if (!password.matches(".*\\d.*")) {
            showAlertErrore("Errore", "La password deve contenere almeno un numero.");
            return false;
        }

        // Verifica se la password contiene almeno una lettera maiuscola
        if (!password.matches(".*[A-Z].*")) {
            showAlertErrore("Errore", "La password deve contenere almeno una lettera maiuscola.");
            return false;
        }

        // Verifica se la password contiene almeno una lettera minuscola
        if (!password.matches(".*[a-z].*")) {
            showAlertErrore("Errore", "La password deve contenere almeno una lettera minuscola.");
            return false;
        }

        // Verifica se la password contiene almeno un carattere speciale
        if (!password.matches(".*[!@#$%^&*(),.?\\\\\":{}|<>].*")) {
            showAlertErrore("Errore", "La password deve contenere almeno un carattere speciale.");
            return false;
        }
        //Password Valida
        return true;
    }

        // Metodo per chiudere la finestra attuale
        private void closeCurrentWindow () {
            Stage currentStage = (Stage) stackPane.getScene().getWindow();
            currentStage.close();
        }
    }







