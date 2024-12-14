package com.GestioneDB;
import com.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class GestioneUtente {

    private SessionFactory sessionFactory;

    public GestioneUtente() {
        // Crea la SessionFactory utilizzando hibernate.cfg.xml
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }

    public boolean verificaERegistraUtente(String username, String email, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Verifica se l'utente esiste già
            String hql = "FROM User u WHERE u.email = :email";
            User utenteEsistente = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();



            if (utenteEsistente != null) {
                System.out.println("Utente già registrato con questa email.");
                return false;
            }

            // Registra un nuovo utente
            User nuovoUtente = new User(username, email, password);
            session.save(nuovoUtente);

            transaction.commit();
            System.out.println("Utente registrato con successo!");
            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;

        } finally {
            session.close();
        }
    }
    //metodo che scorre nel db e verifica se ci sono email e pass
    public boolean verificaCredenziali(String email, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Verifica se l'utente esiste nel database con l'email fornita
            String hql = "FROM User u WHERE u.email = :email";
            User utente = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (utente == null) {
                // Utente non trovato
                return false;
            }

            // Verifica se la password corrisponde
            if (utente.getPassword().equals(password)) {
                return true;
            } else {
                // Password errata
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;

        } finally {
            session.close();
        }
    }


}