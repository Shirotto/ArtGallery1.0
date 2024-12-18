package com.gallery.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ValidazioneInputTest {
    ValidazioneInput validazioneInput = new ValidazioneInput();
    @Test
    public void validaEmail_DeveDareFalseSeLEmailNonHaIlGiustoPattern() {
        String emailNonValida = "emailnonvalida";
        assertFalse(validazioneInput.validaEmail(emailNonValida));
    }
    @Test
    public void emailValida_DeveDareTrueSeLEmailHaIlGiustoPattern() {
        String emailValida = "emailvalida@libero.it";
        assertTrue(validazioneInput.validaEmailConAlert(emailValida));
    }

    @Test
    void usernameValido_DeveDareFalseSeLUsernameNonHaIlGiustoPattern() {
        String usernameNonValido = "ç*°*°ç*";
        assertFalse(validazioneInput.validaUsername(usernameNonValido));
    }
    @Test
    void usernameValido_DeveDareTrueSeLUsernameHaIlGiustoPattern() {
        String usernameValido = "usernameTest";
        assertTrue(validazioneInput.validaUsername(usernameValido));
    }

    @Test
    void passwordValida_SeLaPasswordRisultaVuotaDeveDareFalse() {
        String passVuota = "";
        assertEquals("Password vuota",validazioneInput.validaPassword(passVuota));
    }

    @Test
    void passwordValida_SeLaPasswordRisultaSenzaAlmenoUnNumeroDeveDarefalse() {
        String passSenzaNumeri = "passwordsenzaNumeri@";
        assertEquals("Password senza numeri",validazioneInput.validaPassword(passSenzaNumeri));
    }


    @Test
    void passwordValida_SeLaPasswordRisultaAvereMenoDi8CaratteriDeveDareFalse() {
        String passBreve = "pass0@";
        assertEquals("Password corta",validazioneInput.validaPassword(passBreve));
    }
    @Test
    void passwordValida_SeLaPasswordRisultaContenereSpaziDeveDareFalse() {
        String passConSpazi = "password con spazi 0@";
        assertEquals("Password con spazi",validazioneInput.validaPassword(passConSpazi));
    }
    @Test
    void passwordValida_SeLaPasswordRisultaSenzaCaratteriMaiuscoliDeveDareFalse() {
        String passSenzaLettereMaiuscole = "passwordsenzaletteremaiuscole0@";
        assertEquals("Password senza maiuscole",validazioneInput.validaPassword(passSenzaLettereMaiuscole));
    }
    @Test
    void passwordValida_SeLaPasswordRisultaSenzaCaratteriMinuscoliDeveDareUnFalse() {
        String passSenzaMinuscole = "PASSWORDSENZAMINUSCOLE0@";
        assertEquals("Password senza minuscole",validazioneInput.validaPassword(passSenzaMinuscole));
    }
    @Test
    void passwordValida_eLaPasswordRisultaSenzaCaratteriSpecialiDeveDareFalse() {
        String passSenzaCaratteriSpeciali = "passwordsenzaCaratteriSpeciali0";
        assertEquals("Password senza caratteri speciali",validazioneInput.validaPassword(passSenzaCaratteriSpeciali));
    }
    @Test
    void passwordValida_SeLaPasswordRisultaAvereIlGiustoPatterDeveDareTrue() {
        String passCorretta = "PasswordCorretta0@";
        assertEquals("Password valida",validazioneInput.validaPassword(passCorretta));
    }



}