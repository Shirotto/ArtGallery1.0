package com.gallery.gui;

import com.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfiloApplication {

    private AlertInfo alert = new AlertInfo();


    // Metodo per aprire il profilo in una nuova finestra
    public void openProfile(User currentUser) {
        try {
            // Carica il file FXML del profilo
            Stage profileStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profilo/profilo-view.fxml"));
            AnchorPane root = loader.load();
            ProfiloController profiloController = loader.getController();
            profiloController.setUserData(currentUser);
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
