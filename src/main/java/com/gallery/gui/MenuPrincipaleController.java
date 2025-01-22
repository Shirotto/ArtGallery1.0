package com.gallery.gui;

import com.entity.Opera;
import com.entity.User;
import com.util.HibernateUtil;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class MenuPrincipaleController {

    @FXML
   private WebView webView;

    private User currentUser;



    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        //mi fa visualizzare gli alert definiti nel html
        webEngine.setOnAlert(event -> {
            String message = event.getData();

            System.out.println("Alert JavaScript: " + message);
            // Oltre alla console, puoi visualizzare l'alert con una finestra JavaFX
            AlertInfo.showAlertInfo("Alert JavaScript: ",message);
        });

        String htmlFilePath = getClass().getResource("/com/gallery/gui/menuprincipale/menu.html").toExternalForm();
        if (htmlFilePath == null) {
            System.err.println("File HTML non trovato.");
            return;
        }
        webEngine.load(htmlFilePath);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("ControllerPrincipale", this);
                Windows.aggiornaSezioneGalleria(webView);// fa in modo che le opere restino salvate alla riapertura
                Profilo.updateHTML(currentUser,webView);
            }
        });
    }

    @FXML
    public void exitApp() {
        System.exit(0);
    }

    @FXML
    public void logout() {
        Profilo.logout(webView);

    }

    public void setUserData(User user) {
        this.currentUser = user;

        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("errore in setUserData");
                Profilo.updateHTML(user, webView);


                // Espone il controller Java a JavaScript
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("ControllerPrincipale", this);  // Espone il controller Java



            }
        });
    }

    @FXML
    public void updateProfilo(String nuovoUsername, String nuovaEmail, String nuovaPassword) {
        Profilo.updateProfileData(currentUser,nuovoUsername, nuovaEmail, nuovaPassword, webView);
    }

    public void setUser(User user) {
        this.currentUser = user;

        if (currentUser == null) {
            System.err.println("Errore: Utente corrente è null!");
            return;
        }

        System.out.println("Utente impostato: " + currentUser.getUsername());

    }




    public void salvaOpera(String titolo, String autore, int anno, String tecnica, String descrizione, String imageDataBase64, String dimensione) {
        byte[] immagine = java.util.Base64.getDecoder().decode(imageDataBase64.split(",")[1]);
        Profilo.assegnaOpera( titolo,  autore,  anno,  tecnica,currentUser, descrizione,  imageDataBase64,  dimensione);
        webView.getEngine().reload();
        // Subito dopo il salvataggio, aggiorna la galleria senza ricaricare la pagina
        Windows.aggiornaSezioneGalleria(webView);
    }
    public void showProfileSection() {
        // 1) “mostra” la sezione nel WebView (chiamando la tua showSection('profile-section') lato JS)
         webView.getEngine().executeScript("showSection('profile-section')");

        // 2) e poi reinietti le opere in ‘opere-caricate-row’
        Profilo.mostraOpereUtente(currentUser,webView);

    }

    public boolean modificaOpera(int operaId, String titolo, String autore, int anno, String tecnica, String descrizione, String dimensione) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Cerca l'opera nel database
            Opera opera = session.get(Opera.class, operaId);

            if (opera == null) {
                System.out.println("Opera con ID " + operaId + " non trovata.");
                return false;
            }

            // Modifica i campi dell'opera
            opera.setNome(titolo);
            opera.setAutore(autore);
            opera.setAnno(anno);
            opera.setTecnica(tecnica);
            opera.setDescrizione(descrizione);
            opera.setDimensione(dimensione);

            // Aggiorna l'opera nel database
            session.update(opera);
            transaction.commit();
            Windows.aggiornaSezioneProfilo(webView,currentUser);
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Log dettagliato dell'eccezione
            return false;
        }
    }


    public boolean eliminaOpera(int operaId) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Cerca l'opera nel database
            Opera opera = session.get(Opera.class, operaId);

            if (opera == null) {
                System.out.println("Opera con ID " + operaId + " non trovata.");
                return false;
            }

            // Procedi con l'eliminazione dell'opera
            session.delete(opera);

            transaction.commit();
            Windows.aggiornaSezioneGalleria(webView);
            AlertInfo.showAlertInfo("Successo!", "Opera " + opera.getNome() + " Eliminata correttamente!");
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();  // Log dettagliato dell'eccezione
            return false;
        }
    }




}
