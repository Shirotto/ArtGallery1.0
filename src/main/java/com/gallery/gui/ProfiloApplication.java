package com.gallery.gui;

import com.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ProfiloApplication {

    private final AlertInfo alert = new AlertInfo();

    // Metodo per aprire il profilo in una nuova finestra
    public void openProfile(User currentUser) {
        try {
            URL fxmlLocation = getClass().getResource("/com/gallery/gui/profilo/profilo-view.fxml");
            if (fxmlLocation == null) {
                System.err.println("File FXML non trovato: /com/gallery/gui/profilo/profilo-view.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            AnchorPane root = loader.load();
            ProfiloController profiloController = loader.getController();
            if (profiloController != null) {
                profiloController.setUserData(currentUser);
            } else {
                System.err.println("Il controller ProfiloController non è stato inizializzato correttamente.");
            }
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
