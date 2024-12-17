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
            Stage menuStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profilo/profilo-view.fxml"));
            AnchorPane root = loader.load();
            ProfiloController profiloController = loader.getController();
            profiloController.setUserData(currentUser);  // Passa l'utente al controller del profilo
            Scene scene = new Scene(root);
            menuStage.setTitle("Profilo");
            menuStage.setScene(scene);
            menuStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alert.showAlertErrore("Errore", "Impossibile aprire il profilo.");
        }
    }




}
