package com.gallery.gui;

import com.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ProfiloApplication {

    private AlertInfo alert = new AlertInfo();


    // Metodo per aprire il profilo in una nuova finestra
    public void openProfile(User currentUser) {
        try {
            // Verifica il percorso del file FXML
            URL fxmlLocation = getClass().getResource("/com/gallery/gui/profilo/profilo-view.fxml");
            if (fxmlLocation == null) {
                System.err.println("File FXML non trovato: /com/gallery/gui/profilo/profilo-view.fxml");
                return;
            }
            System.out.println("Caricamento FXML da: " + fxmlLocation);

            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            AnchorPane root = loader.load();

            // Ottieni il controller e imposta i dati dell'utente
            ProfiloController profiloController = loader.getController();
            if (profiloController != null) {
                profiloController.setUserData(currentUser);
            } else {
                System.err.println("Il controller ProfiloController non è stato inizializzato correttamente.");
            }

            // Configura la finestra del profilo
            Stage profileStage = new Stage();
            Scene scene = new Scene(root, 800, 600);
            profileStage.setScene(scene);
            profileStage.setTitle("Profilo Utente");
            profileStage.setMinWidth(800);
            profileStage.setMinHeight(600);
            profileStage.setResizable(false);
            profileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alert.showAlertErrore("Errore", "Impossibile aprire il profilo.");
        }
    }
}
