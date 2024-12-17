package com.GestioneDB;
import com.entity.User;
import com.gallery.gui.AlertInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class GestioneUtente {

    private SessionFactory sessionFactory;
    private AlertInfo alertInfo = new AlertInfo();

    public GestioneUtente() {
        // Crea la SessionFactory utilizzando hibernate.cfg.xml
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }
    //Verifica e registra se l'utente esiste gia nel db (Fase Registazione)
    public void RegistraUtente(String username, String email, String password) {
        if (verificaSeUnUtenteEsiste(email)){
            alertInfo.showAlertErrore("utente già registrato","Esiste già un utente con questa email");
            return;
        }
        Session session = sessionFactory.openSession();  // creazione sessione (Permette di interagire con il db)
        Transaction transaction = session.beginTransaction();
        User nuovoUtente = new User(username, email, password);
        session.persist(nuovoUtente);

        //Termine transazione
        transaction.commit();
        session.close();



    }


    //Metodo che scorre nel db e verifica se ci sono email e pass ("FaseLogin")
    public boolean verificaCredenziali(String email, String password) {
        if(!verificaSeUnUtenteEsiste(email)) return false;
        User utente = getUserByEmail(email);
        return utente.getPassword().equals(password);


    }
    // Metodo per ottenere l'utente tramite email (Per il profilo utente)
    public User getUserByEmail(String email) {
        if (verificaSeUnUtenteEsiste(email)) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            // recupera l'utente dal DB
            String hql = "FROM User u WHERE u.email = :email";
            User utente = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            transaction.commit();
            session.close();
            return utente;
        }
        return null;
    }


    private boolean verificaSeUnUtenteEsiste(String emailUtente) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Verifica se l'utente esiste nel database con l'email fornita
        String hql = "FROM User u WHERE u.email = :email";
        User utente = session.createQuery(hql, User.class)
                .setParameter("email", emailUtente)
                .uniqueResult();
        transaction.commit();
        session.close();
        return utente != null;
    }


}