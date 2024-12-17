package com.Service;
import com.GestioneDB.GestioneUtente;
import com.gallery.gui.ValidazioneInput;
//classe che fa da tramite tra  "lato grafico" e lato "gestionalr"


public class LogicaLogReg {

    private final GestioneUtente gestioneUtente = new GestioneUtente();
    private final ValidazioneInput validazioneInput = new ValidazioneInput();

    public boolean verificaSintassiReg(String username, String email, String password) {
            // Se il "check" sulla sintassi è valido mando al db per la verifica(se c'è già)
            if (!validazioneInput.usernameValido(username) ||
                    !validazioneInput.emailValida(email) ||
                    !validazioneInput.passwordValida(password)) {
                return false;
            }
            // Mando l'utente al gestore del db
            return gestioneUtente.verificaERegistraUtente(username, email, password);
        }
    }


