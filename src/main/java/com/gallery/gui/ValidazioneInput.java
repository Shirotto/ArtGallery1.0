package com.gallery.gui;
// i metodi "valida... " sono logici
// i medoti "...Valida" sono di gui
public class ValidazioneInput {

    private AlertInfo alert = new AlertInfo();

    public boolean validaInput(String nome, String email, String password) {
        return validaUsername(nome) && validaEmail(email) && validaPassword(password).equals("Password valida");
    }
    public void validaInputConAlert(String nome, String email, String password) {
        validaUsernameConAlert(nome);
        validaEmailConAlert(email);
        validaPasswordConAlert(password);
    }

    private void mostraAlertErrore(String titolo, String messaggio) {
        alert.showAlertErrore(titolo, messaggio);
    }

    public boolean validaEmail(String email) {
        return email != null && !email.isEmpty() &&
                email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public boolean validaEmailConAlert(String email) {
        if (!validaEmail(email)) {
            mostraAlertErrore("Email non valida", "Inserisci un'email corretta.");
            return false;
        }
        return true;
    }

    public boolean validaUsername(String username) {
        return username != null && !username.isEmpty() &&
                username.matches("^[a-zA-Z0-9._-]+$");
    }

    public void validaUsernameConAlert(String username) {
        if (!validaUsername(username)) {
            mostraAlertErrore("Username non valido", "Inserisci un nome utente valido.");

        }
    }

    public String validaPassword(String password) {
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

    public void validaPasswordConAlert(String password) {
        if (!validaPassword(password).equals("Password valida")) {
            mostraAlertErrore("ERRORE", validaPassword(password));
        }
    }
}



