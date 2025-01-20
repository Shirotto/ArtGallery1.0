package com.gallery.gui;

import com.GestioneDB.GestioneOpere;
import com.entity.Opera;
import com.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class WindowsController {
public static void apriFinestraLogin(){

    try {
        FXMLLoader loader = new FXMLLoader(WindowsController.class.getResource("login/login-view.fxml"));
        Stage stage = new Stage(); // Crea una nuova finestra
        stage.setTitle("");
        stage.setScene(new Scene(loader.load())); // Carica la scena
        stage.setWidth(570);
        stage.setHeight(580);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public static void apriMenuPrincipale(User currentUser) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowsController.class.getResource("/com/gallery/gui/menuprincipale/menu-view.fxml"));
            Scene scene = new Scene(loader.load(), 830, 650);
            Stage menuStage = new Stage();
            menuStage.setScene(scene);
            MenuPrincipaleController menuController = loader.getController();
            menuController.setUser(currentUser);
            menuStage.setTitle("");
            menuStage.setResizable(false);
            menuStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il caricamento di menu-view.fxml", e);
        }

    }

    public static void aggiornaSezioneGalleria(WebView webView) {
        List<Opera> opere = GestioneOpere.getAllOpere();
        StringBuilder scriptBuilder = new StringBuilder("galleryRow = document.getElementById('row-cat1');");

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
                    .append("data-user='").append(GestioneOpere.getNomeProprietario(opera).replace("'", "\\'")).append("'>")
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
    public static void mostraSezioneProfilo(WebView webView,User currentUser) {
        webView.getEngine().executeScript("showSection('profile-section')");
        ProfiloController.mostraOpereUtente(currentUser, webView);
    }
    public static void aggiornaSezioneProfilo(WebView webView,User currentUser){
    ProfiloController.mostraOpereUtente(currentUser,webView);
    webView.getEngine().reload();
    }

        public static void chiudiFinestraCorrente(StackPane stackPane){
        Stage currentStage = (Stage) stackPane.getScene().getWindow();
        currentStage.close();
    }

}