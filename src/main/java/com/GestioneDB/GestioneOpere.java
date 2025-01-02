package com.GestioneDB;

import com.entity.Opera;
import com.entity.User;
import com.gallery.gui.AlertInfo;
import com.gallery.gui.MenuPrincipaleController;
import javafx.scene.web.WebView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;


public class GestioneOpere {


    static AlertInfo alertInfo = new AlertInfo();

    private static SessionFactory sessionFactory;

    static {
        // Configura Hibernate
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Opera.class)
                .buildSessionFactory();
    }

    // Metodo per salvare l'opera nel database
    public static void salvaOperaDb(String nome, String autore, int anno, String tecnica, User user, String descrizione, byte[] immagine) {
        System.out.println("salvaOperaDb invocato!");
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Crea una nuova opera
            Opera opera = new Opera(nome, autore, anno, tecnica, user, descrizione, immagine);

            // Salva l'opera nel database
            session.save(opera);

            // Commit della transazione
            session.getTransaction().commit();

            alertInfo.showAlertInfo("Successo!", "Opera Salvata Correttamente!");

        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }


    //metodo per recuperare le opere di uno specifico user
    public static List<Opera> getOpereByUser(User user) {
        if (user == null) {
            System.err.println("Errore: l'utente corrente è null!");
            return new ArrayList<>();
        }

        Session session = null;
        List<Opera> opere = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Query per recuperare le opere dell'utente specifico
            opere = session.createQuery("FROM Opera WHERE user = :user", Opera.class)
                    .setParameter("user", user)
                    .getResultList();

            session.getTransaction().commit();
            System.out.println("Numero di opere recuperate: " + (opere != null ? opere.size() : 0));
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return opere;
    }


    //metodo che mi prende le opere dal db
    public static List<Opera> getAllOpere() {
        Session session = null;
        List<Opera> opere = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();


            // Esegue una query per ottenere tutte le opere
            opere = session.createQuery("from Opera", Opera.class).getResultList();


            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();

        }
        // Log per verificare se le opere sono recuperate correttamente
        System.out.println("Opere recuperate dal database: " + (opere != null ? opere.size() : 0));
        return opere;
    }


}
