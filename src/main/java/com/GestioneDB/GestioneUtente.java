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
    //Verifica e registra se l'utente esiste gia nel db (Fase Registazione)
    public boolean verificaERegistraUtente(String username, String email, String password) {
        Session session = sessionFactory.openSession();  // creazione sessione (Permette di interagire con il db)
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();  // inizio transazione (operazione atomica)

            //Crezione HQL che verifica se la mail esiste nel db
            String hql = "FROM User u WHERE u.email = :email";
            User utenteEsistente = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (utenteEsistente != null) {
                System.out.println("Utente già registrato con questa email.");
                return false;

            }

            // Crea utente e salva sessione
            User nuovoUtente = new User(username, email, password);
            session.save(nuovoUtente);

            //Termine transazione
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


    //Metodo che scorre nel db e verifica se ci sono email e pass ("FaseLogin")
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
    // Metodo per ottenere l'utente tramite email (Per il profilo utente)
    public User getUserByEmail(String email) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        User utente = null;
        try {
            transaction = session.beginTransaction();

            String hql = "FROM User u WHERE u.email = :email";
            utente = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();

            transaction.commit();
            System.out.println(utente.getUsername());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return utente;

    }


}