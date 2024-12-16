package com.gallery.gui;
import com.GestioneDB.GestioneUtente;
import com.Service.LogicaLogReg;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    private AlertInfo alert = new AlertInfo();

    private MenuPrincipaleApplication menuPrincipaleApplication = new MenuPrincipaleApplication();

    private GestioneUtente gestione = new GestioneUtente(); // Inizializza GestioneUtente

    private LogicaLogReg gestioneLR = new LogicaLogReg();


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

    // Gestione evento di login
    public void handleLoginButtonClick(String email, String password) {

        boolean loginsuccesso = gestioneLR.effettuaLogin(email, password);
        if (loginsuccesso) {
            alert.showAlertInfo("Login riuscito", "Accesso effettuato correttamente!");
            apriMenuPrincipale();
        } else {
            alert.showAlertErrore("Errore", "Email o password non valide.");

        }
    }

    // Gestione evento registrazione
    public void handleSignUpButtonClick(String name, String email, String password) {

        boolean registrazioneSuccesso = gestioneLR.effettuaRegistrazione(name, email, password);
        if (registrazioneSuccesso) {
            // Se la registrazione è avvenuta con successo
            alert.showAlertInfo("Registrazione riuscita", "Benvenuto, registrazione completata!");
            closeCurrentWindow();
            riapriFinestraLogin();
        } else {
            //altrimenti
            alert.showAlertErrore("Errore", "Errore durante la registrazione. Controlla i dati.");
        }
    }


    // Metodo per chiudere la finestra attuale
    private void closeCurrentWindow() {
        Stage currentStage = (Stage) stackPane.getScene().getWindow();
        currentStage.close();
    }

    private void apriMenuPrincipale() {
        System.out.println("Apertura del menu principale...");
        menuPrincipaleApplication.openMainMenu();
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


        }
    }
}










