package com.Service;
import com.GestioneDB.GestioneUtente;
import com.gallery.gui.ValidazioneInput;

public class LogicaLogReg {


        private final GestioneUtente gestioneUtente = new GestioneUtente();
        private final ValidazioneInput validazioneInput = new ValidazioneInput();

        public boolean effettuaLogin(String email, String password) {
            // Validazione dei dati Log
            if (!validazioneInput.emailValida(email) || !validazioneInput.passwordValida(password)) {
                return false;
            }
            // Verifica credenziali
            return gestioneUtente.verificaCredenziali(email, password);
        }

        public boolean effettuaRegistrazione(String username, String email, String password) {
            // Validazione dei dati Reg
            if (!validazioneInput.usernameValido(username) ||
                    !validazioneInput.emailValida(email) ||
                    !validazioneInput.passwordValida(password)) {
                return false;
            }
            // Registra utente
            return gestioneUtente.verificaERegistraUtente(username, email, password);
        }
    }


