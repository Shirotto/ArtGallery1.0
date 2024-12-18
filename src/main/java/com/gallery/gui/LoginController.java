package com.gallery.gui;
import com.GestioneDB.GestioneUtente;
import com.entity.User;
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

    final AlertInfo alert = new AlertInfo();

    GestioneUtente gestioneUtente = new GestioneUtente("hibernate.cfg.xml");

    private User currentUser;


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

        if (gestioneUtente.verificaCredenziali(email, password)) {
            //recupero dati utente dopo login
            currentUser = gestioneUtente.getUserByEmail(email);
            apriMenuPrincipale();
        } else {
            alert.showAlertErrore("Errore", "Email o password non valide.");

        }
    }

    // Gestione evento registrazione
    public void handleSignUpButtonClick(String name, String email, String password) {
        if (gestioneUtente.registraUtente(name,email,password)) {
            closeCurrentWindow();
            riapriFinestraLogin();

        }
        }

    // Metodo per chiudere la finestra attuale
    private void closeCurrentWindow() {
        Stage currentStage = (Stage) stackPane.getScene().getWindow();
        currentStage.close();
    }

    private void apriMenuPrincipale() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipale/menu-view.fxml"));
            Stage menuStage = new Stage();
            menuStage.setScene(new Scene(loader.load()));

            //Ottieni il controller del menu principale
            MenuPrincipaleController menuController = loader.getController();

            //Passo l'utente corrente al controller
            menuController.setUser(currentUser);

            menuStage.setTitle("Menu Principale");
            menuStage.show();

            // Chiudi la finestra di login
            closeCurrentWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Riapre la finestra login dopo essersi registrati
    private void riapriFinestraLogin() {

        try {
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











