package com.gallery.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidazioneInputTest {
    AlertInfoTest ait = new AlertInfoTest();
    public boolean emailValidaTEST(String email) {
        if (email == null || email.isEmpty() ||
                !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            ait.showAlertErroreTEST("Email non valida", "Inserisci un'email corretta.");

            return false;
        }
        return true;
    }
    public boolean usernameValidoTEST(String username) {
        if (username == null || username.isEmpty() ||
                !username.matches("^[a-zA-Z0-9._-]+$")) {
            ait.showAlertErroreTEST("Username non valido", "Inserisci un nome utente valido.");
            return false;
        }
        return true;
    }

    public String passwordValidaTEST(String password) {
        if (password == null || password.isEmpty()) {
            return ait.showAlertErroreTEST("Password non valida", "La password non può essere vuota.");

        }
        if (password.length() < 8) {
            return ait.showAlertErroreTEST("Password non valida", "La password deve contenere almeno 8 caratteri.");

        }
        if (password.contains(" ")) {
            return ait.showAlertErroreTEST("Password non valida", "La password non può contenere spazi.");

        }
        if (!password.matches(".*\\d.*")) {
            return ait.showAlertErroreTEST("Password non valida", "La password deve contenere almeno un numero.");

        }
        if (!password.matches(".*[A-Z].*")) {
            return ait.showAlertErroreTEST("Password non valida", "La password deve contenere almeno una lettera maiuscola.");

        }
        if (!password.matches(".*[a-z].*")) {
            return ait.showAlertErroreTEST("Password non valida", "La password deve contenere almeno una lettera minuscola.");

        }
        if (!password.matches(".*[!@#$%^&*(),.?\\\\\":{}|<>].*")) {
        return ait.showAlertErroreTEST("Password non valida", "La password deve contenere almeno un carattere speciale (!@#$%^&*...).");

        }

        return "Password valida";
    }

    @Test
    void SeEmailNonHaIlGiustoPatternDeveDareUnAlert() {
        String email = "emailnonvalida";
        assertFalse(emailValidaTEST(email));
    }

    @Test
    void SeLUserNameNonHaIlGiustoPatternDeveDareUnAlert() {
        String email = "usernameNonValido";
        assertFalse(emailValidaTEST(email));
    }

    @Test
    void SeLaPasswordRisultaVuotaDeveDareUnAlert() {
        String passVuota = "";

        assertEquals("Password non valida La password non può essere vuota.",passwordValidaTEST(passVuota));
    }

    @Test
    void SeLaPasswordRisultaSenzaAlmenoUnNumeroDeveDareUnAlert() {
        String passSenzaNumeri = "passwordsenzaNumeri";

        assertEquals("Password non valida La password deve contenere almeno un numero.",passwordValidaTEST(passSenzaNumeri));
    }


    @Test
    void SeLaPasswordRisultaAvereMenoDi8CaratteriDeveDareUnAlert() {
        String passBreve = "pass0@";

        assertEquals("Password non valida La password deve contenere almeno 8 caratteri.",passwordValidaTEST(passBreve));
    }
    @Test
    void SeLaPasswordRisultaContenereSpaziDeveDareUnAlert() {
        String passConSpazi = "password con spazi 0@";

        assertEquals("Password non valida La password non può contenere spazi.",passwordValidaTEST(passConSpazi));
    }
    @Test
    void SeLaPasswordRisultaSenzaCaratteriMaiuscoliDeveDareUnAlert() {
        String passSenzaLettereMaiuscole = "passwordsenzaletteremaiuscole0@";

        assertEquals("Password non valida La password deve contenere almeno una lettera maiuscola.",passwordValidaTEST(passSenzaLettereMaiuscole));
    }
    @Test
    void SeLaPasswordRisultaSenzaCaratteriMinuscoliDeveDareUnAlert() {
        String passSenzaMinuscole = "PASSWORDSENZAMINUSCOLE0@";

        assertEquals("Password non valida La password deve contenere almeno una lettera minuscola.",passwordValidaTEST(passSenzaMinuscole));
    }
    @Test
    void SeLaPasswordRisultaSenzaCaratteriSpecialiDeveDareUnAlert() {
        String passSenzaCaratteriSpeciali = "passwordsenzaCaratteriSpeciali0";

        assertEquals("Password non valida La password deve contenere almeno un carattere speciale (!@#$%^&*...).",passwordValidaTEST(passSenzaCaratteriSpeciali));
    }
    @Test
    void SeLaPasswordRisultaAvereIlGiustoPatterNonDeveDareAlert() {
        String passCorretta = "PasswordCorretta0@";
        assertEquals("Password valida",passwordValidaTEST(passCorretta));
    }

}