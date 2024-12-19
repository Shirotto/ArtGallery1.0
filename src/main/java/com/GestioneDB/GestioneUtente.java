package com.GestioneDB;
import com.entity.User;
import com.gallery.gui.AlertInfo;
import com.gallery.gui.ValidazioneInput;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class GestioneUtente {

    public SessionFactory sessionFactory;
    AlertInfo alertInfo = new AlertInfo();
    ValidazioneInput validazioneInput = new ValidazioneInput();

    public GestioneUtente(String hibernate) {
        // Crea la SessionFactory utilizzando hibernate.cfg.xml
        this.sessionFactory = new Configuration().configure(hibernate).addAnnotatedClass(User.class).buildSessionFactory();
    }
    //Verifica e registra se l'utente esiste gia nel db (Fase Registazione)
    public boolean registraUtente(String username, String email, String password) {

        if (verificaSeUnUtenteEsisteByUsername(username)){
            alertInfo.showAlertErrore("utente già registrato","Esiste già un utente con questo username");
            return false;
        }

        if (verificaSeUnUtenteEsisteByEmail(email)){
            alertInfo.showAlertErrore("utente già registrato","Esiste già un utente con questa email");
            return false;
        }
        if (validazioneInput.validaInput(username,email,password)) {
            Session session = sessionFactory.openSession();  // creazione sessione (Permette di interagire con il db)
            Transaction transaction = session.beginTransaction();
            User nuovoUtente = new User(username, email, password);
            session.persist(nuovoUtente);

            transaction.commit();
            session.close();
            alertInfo.showAlertInfo( "utente registrato con successo","BENVENUTO "+ username);
            return true;
        }
        validazioneInput.validaInputConAlert(username,email,password);
        //alertInfo.showAlertErrore("Errore", "Errore durante la registrazione. Controlla i dati.");
        return false;


    }


    //Metodo che scorre nel db e verifica se ci sono email e pass ("FaseLogin")
    public boolean verificaCredenziali(String email, String password) {
        if(!verificaSeUnUtenteEsisteByEmail(email)) return false;
        User utente = getUserByEmail(email);
        return utente.getPassword().equals(password);


    }
    // Metodo per ottenere l'utente tramite email (Per il profilo utente)
    public User getUserByEmail(String email) {
        if (verificaSeUnUtenteEsisteByEmail(email)) {
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

    public boolean verificaSeUnUtenteEsiste(String username,String emailUtente) {
        return verificaSeUnUtenteEsisteByEmail(emailUtente) || verificaSeUnUtenteEsisteByUsername(username);
    }
    public boolean verificaSeUnUtenteEsisteByEmail(String emailUtente) {
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
    public boolean verificaSeUnUtenteEsisteByUsername(String username) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Verifica se l'utente esiste nel database con l'email fornita
        String hql = "FROM User u WHERE u.username = :username";
        User utente = session.createQuery(hql, User.class)
                .setParameter("username", username)
                .uniqueResult();
        transaction.commit();
        session.close();
        return utente != null;
    }


}