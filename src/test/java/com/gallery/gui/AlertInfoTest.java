package com.gallery.gui;

import org.junit.jupiter.api.Test;

class AlertInfoTest {

    public String showAlertErroreTEST(String title, String message) {
        /*Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();*/
        return title +" "+ message;
    }

}