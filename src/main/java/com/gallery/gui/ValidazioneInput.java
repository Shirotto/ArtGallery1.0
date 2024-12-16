package com.gallery.gui;


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

    public boolean passwordValida(String password) {
        if (password == null || password.isEmpty() || password.length() < 8 ||  password.contains(" ") ||
                !password.matches(".*\\d.*") || !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*[!@#$%^&*(),.?\\\\\":{}|<>].*")) {
            alert.showAlertErrore("Password non valida", "Inserisci una password conforme ai requisiti.");
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


}
