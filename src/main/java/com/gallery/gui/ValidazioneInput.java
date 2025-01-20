package com.gallery.gui;
// i metodi "valida... " sono logici
// i medoti "...Valida" sono di gui
public class ValidazioneInput {


    public static  boolean validaInput(String nome, String email, String password) {
        return validaUsername(nome) && validaEmail(email) && validaPassword(password).equals("Password valida");
    }
    public static boolean validaInputConAlert(String nome, String email, String password) {
       return  (validaUsernameConAlert(nome) ||
        validaEmailConAlert(email) ||
        validaPasswordConAlert(password));
    }

    public static boolean validaEmail(String email) {
        return email != null && !email.isEmpty() &&
                email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean validaEmailConAlert(String email) {
        if (!validaEmail(email)) {
            AlertInfo.showAlertErrore("Email non valida", "Inserisci un'email corretta.");
            return false;
        }
        return true;
    }

    public static boolean validaUsername(String username) {
        return username != null && !username.isEmpty() &&
                username.matches("^[a-zA-Z0-9._-]+$");
    }

    public static boolean validaUsernameConAlert(String username) {
        if (!validaUsername(username)) {
            AlertInfo.showAlertErrore("Username non valido", "Inserisci un nome utente valido.");
            return false;
        }
        return true;
    }

    public static String validaPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Password vuota";
        }
        if (password.length() < 8) {
            return "Password corta";
        }
        if (password.contains(" ")) {
            return "Password con spazi";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password senza numeri";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password senza maiuscole";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password senza minuscole";
        }
        if (!password.matches(".*[!@#$%^&*(),.?\\\\\":{}|<>].*")) {
            return "Password senza caratteri speciali";
        }

        return "Password valida";
    }

    public static boolean validaPasswordConAlert(String password) {
        if (!validaPassword(password).equals("Password valida")) {
            AlertInfo.showAlertErrore("ERRORE", validaPassword(password));
            return false;
        }
        return true;
    }
}



