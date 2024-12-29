package com.GestioneDB;

import com.entity.Opera;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class GestioneOpere {

    private static SessionFactory sessionFactory;

    static {
        // Configura Hibernate
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Opera.class)
                .buildSessionFactory();
    }

    // Metodo per salvare l'opera nel database
    public static void salvaOperaDb(String nome, String descrizione, byte[] immagine) {
        System.out.println("salvaOpera invocato!");
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Crea una nuova opera
            Opera opera = new Opera(nome, descrizione, immagine);

            // Salva l'opera nel database
            session.save(opera);

            // Commit della transazione
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
