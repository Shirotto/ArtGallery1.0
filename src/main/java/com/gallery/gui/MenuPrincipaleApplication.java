package com.gallery.gui;

import com.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuPrincipaleApplication {


    private AlertInfo alert = new AlertInfo();

    // Metodo per aprire il menu principale in una nuova finestra
    public void openMainMenu() {
        try {
            Stage menuStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipale/menu-view.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            menuStage.setTitle("Menu Principale");
            menuStage.setScene(scene);
            menuStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alert.showAlertErrore("Errore", "Impossibile aprire il menu principale.");
        }
    }

}