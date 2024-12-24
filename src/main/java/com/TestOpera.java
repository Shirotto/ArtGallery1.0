package com;

import com.entity.Opera;
import com.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static com.util.ConvertitoreImmagini.immagineInByte;


public class TestOpera {
    public static void main(String[] args) {
        // Configura Hibernate per gestire User e Opera
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Opera.class)
                .buildSessionFactory();

        Session session = null;

        try {
            // Apri una nuova sessione
            session = sessionFactory.openSession();

            // Inizia la transazione
            session.beginTransaction();

            // Recupera l'utente con ID 1 dal database
            Long userId = 1L;
            User user = session.get(User.class, userId);

            if (user == null) {
                System.out.println("Utente con ID " + userId + " non trovato.");
                return;
            }

            System.out.println("Utente trovato: " + user);

            // Crea una nuova Opera
            Opera opera = new Opera();
            opera.setNome("La Gioconda");
            opera.setDescrizione("Un capolavoro del Rinascimento italiano.");
            opera.setUser(user); // Associa l'utente all'opera
            byte[] immagineOpera = immagineInByte("src/main/java/com/monalisa.jpg");
            //try {
                //File fileImmagine = new File("src/main/java/com/monalisa.jpg");
                //FileInputStream fis = new FileInputStream(fileImmagine);
                //immagineOpera = fis.readAllBytes();
                //fis.close();
            //} catch (IOException e) {
                //System.err.println("Errore nella lettura del file immagine: " + e.getMessage());
               // return;
            //}
            opera.setImmagine(immagineOpera);

            // Salva l'oggetto Opera nel database
            session.save(opera);

            // Commit della transazione
            session.getTransaction().commit();

            System.out.println("Opera salvata con successo: " + opera);
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
            sessionFactory.close();
        }
    }
}

