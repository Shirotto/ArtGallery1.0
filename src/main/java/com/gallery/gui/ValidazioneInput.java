package com.gallery.gui;


import java.util.LinkedHashMap;
import java.util.Map;

public class ValidazioneInput {

    private AlertInfo alert = new AlertInfo();


    public boolean emailValida(String email) {
        if (email == null || email.isEmpty() ||
                !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            alert.showAlertErrore("Email non valida", "Inserisci un'email corretta.");
            return false;
        }
        return true;
    }

    public boolean usernameValido(String username) {
        if (username == null || username.isEmpty() ||
                !username.matches("^[a-zA-Z0-9._-]+$")) {
            alert.showAlertErrore("Username non valido", "Inserisci un nome utente valido.");
            return false;
        }
        return true;
    }

     public boolean passwordValida(String password) {
         if (password == null || password.isEmpty()) {
             alert.showAlertErrore("Password non valida", "La password non può essere vuota.");
             return false;
         }
         if (password.length() < 8) {
             alert.showAlertErrore("Password non valida", "La password deve contenere almeno 8 caratteri.");
             return false;
         }
         if (password.contains(" ")) {
             alert.showAlertErrore("Password non valida", "La password non può contenere spazi.");
             return false;
         }
         if (!password.matches(".*\\d.*")) {
             alert.showAlertErrore("Password non valida", "La password deve contenere almeno un numero.");
             return false;
         }
         if (!password.matches(".*[A-Z].*")) {
             alert.showAlertErrore("Password non valida", "La password deve contenere almeno una lettera maiuscola.");
             return false;
         }
         if (!password.matches(".*[a-z].*")) {
             alert.showAlertErrore("Password non valida", "La password deve contenere almeno una lettera minuscola.");
             return false;
         }
         if (!password.matches(".*[!@#$%^&*(),.?\\\\\":{}|<>].*")) {
             alert.showAlertErrore("Password non valida", "La password deve contenere almeno un carattere speciale (!@#$%^&*...).");
             return false;
         }

         return true; // Password valida
     }





}
