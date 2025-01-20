package com.GestioneDB;
import com.entity.User;
import com.gallery.gui.AlertInfo;
import com.gallery.gui.ValidazioneInput;
import com.util.HibernateUtil;
import com.util.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class GestioneUtente {


    public SessionFactory sessionFactory;


    public GestioneUtente(String hibernate) {
        // Crea la SessionFactory utilizzando hibernate.cfg.xml
        this.sessionFactory = new Configuration().configure(hibernate).addAnnotatedClass(User.class).buildSessionFactory();
    }
    //Verifica e registra se l'utente esiste gia nel db (Fase Registazione)
    public boolean registraUtente(String username, String email, String password) {

        if (verificaSeUnUtenteEsisteByUsername(username)){
            AlertInfo.showAlertErrore("utente già registrato","Esiste già un utente con questo username");
            return false;
        }

        if (verificaSeUnUtenteEsisteByEmail(email)){
            AlertInfo.showAlertErrore("utente già registrato","Esiste già un utente con questa email");
            return false;
        }

        if (ValidazioneInput.validaInput(username, email, password)) {
            // Cripta la password prima di salvarla nel database
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Crea la sessione Hibernate
            Session session = sessionFactory.openSession();  // creazione sessione (Permette di interagire con il db)
            Transaction transaction = session.beginTransaction();

            // Crea il nuovo utente con la password criptata
            User nuovoUtente = new User(username, email, hashedPassword);
            session.persist(nuovoUtente);

            transaction.commit();
            session.close();

            AlertInfo.showAlertInfo("utente registrato con successo", "BENVENUTO " + username);
            return true;
        }
        ValidazioneInput.validaInputConAlert(username, email, password);

        return false;
    }

    //Metodo che scorre nel db e verifica se ci sono email e pass ("FaseLogin")
    public boolean verificaCredenzialiDaccesso(String email, String password) {
        // Verifica se l'utente esiste con l'email
        if (!verificaSeUnUtenteEsisteByEmail(email)) return false;

        // Ottieni l'utente dal database
        User utente = getUserByEmail(email);

        // Usa BCrypt per confrontare la password inserita con quella salvata (hash)
        return PasswordUtil.checkPassword(password, utente.getPassword());
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
        Session session = this.sessionFactory.openSession();
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
    public static void saveUserDataToDatabase(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            System.out.println("Dati utente aggiornati nel database: " + user);
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
    }


}