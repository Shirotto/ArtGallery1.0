package com.GestioneDB;


import com.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class GestioneUtenteTest {
    private static SessionFactory sessionFactory;
    GestioneUtente gestioneUtente = new GestioneUtente("Testhibernate.cfg.xml");


    @BeforeAll
    public static void setUp() {
        sessionFactory = new Configuration()
                .configure("Testhibernate.cfg.xml") // Assicurati che questo file sia corretto
                .addAnnotatedClass(User.class) // Mappaggio alla tua classe User
                .buildSessionFactory();
        Session session = sessionFactory.openSession();  // creazione sessione (Permette di interagire con il db)
        Transaction transaction = session.beginTransaction();
        session.persist(new User("utenteTest", "utenteTest@libero.it", "passwordTest0@"));
        transaction.commit();
        session.close();

    }

    @AfterAll
    public static void clean() {
        sessionFactory = new Configuration()
                .configure("Testhibernate.cfg.xml") // Assicurati che questo file sia corretto
                .addAnnotatedClass(User.class) // Mappaggio alla tua classe User
                .buildSessionFactory();
        Session session = sessionFactory.openSession();  // creazione sessione (Permette di interagire con il db)
        Transaction transaction = session.beginTransaction();

        // Cancella tutti i dati da User o altre tabelle test
        session.createQuery("delete from User").executeUpdate();
        transaction.commit();
        session.close();

    }

    @Test
    public void verificaSeUnUtenteEsisteByUsername_DeveDareTrueSeLUtenteEsisteNelDB() {
        String usernameEsistente = "utenteTest";
        assertTrue(this.gestioneUtente.verificaSeUnUtenteEsisteByUsername(usernameEsistente));

    }

    @Test
    public void verificaSeUnUtenteEsisteByUsername_DeveDareFalseSeLUtenteNonEsisteNelDB() {
        String usernameEsistente = "utenteTestNonEsistente";
        assertFalse(this.gestioneUtente.verificaSeUnUtenteEsisteByUsername(usernameEsistente));

    }

    @Test
    public void verificaSeUnUtenteEsisteByEmail_DeveDareTrueSeLUtenteEsisteNelDB() {
        String emailEsistente = "utenteTest@libero.it";
        assertTrue(this.gestioneUtente.verificaSeUnUtenteEsisteByEmail(emailEsistente));
    }

    @Test
    public void verificaSeUnUtenteEsisteByEmail_DeveDareFalseSeLUtenteNonEsisteNelDB() {
        String emailNonEsistente = "utenteTestNonEsistente@libero.it";
        assertFalse(this.gestioneUtente.verificaSeUnUtenteEsisteByEmail(emailNonEsistente));
    }

    @Test
    public void verificaSeUnUtenteEsiste_DeveDareFalseSeLUtenteNonEsisteNelDB() {
        String emailNonEsistente = "utenteTestNonEsistente@libero.it";
        String usernameNonEsistente = "utenteTestNonEsistente";
        assertFalse(this.gestioneUtente.verificaSeUnUtenteEsiste(usernameNonEsistente, emailNonEsistente));
    }

    @Test
    public void verificaSeUnUtenteEsiste_DeveDareTrueSeLUtenteEsisteNelDB() {
        String emailEsistente = "utenteTest@libero.it";
        String usernameEsistente = "utenteTest";
        assertTrue(this.gestioneUtente.verificaSeUnUtenteEsiste(usernameEsistente, emailEsistente));
    }

    @Test
    public void getUserByEmail_DeveRestituireLUserConLaStessaEmailInserita(){
        String emailDellUtente = "utenteTest@libero.it";
        assertEquals(emailDellUtente,gestioneUtente.getUserByEmail(emailDellUtente).getEmail());
    }
    @Test
    public void getUserByEmail_DeveRestituireNullSeLUtenteNonEsiste(){
        String emailDellUtenteCheNonEsiste = "utenteTestNonEsistente@libero.it";
        String emailTrovata;
        try{ emailTrovata = gestioneUtente.getUserByEmail(emailDellUtenteCheNonEsiste).getEmail();
        } catch (Exception e) {emailTrovata = null;}
        assertNull(emailTrovata);
    }

    @Test
    public void verificaCredenziali_DeveDareTrueSeLecredenzialiSonoEntrambeCorrette(){
        String emailEsistente = "utenteTest@libero.it";
        String passwordCorretta = "passwordTest0@";
        assertTrue(gestioneUtente.verificaCredenzialiDaccesso(emailEsistente,passwordCorretta));
    }
    @Test
    public void verificaCredenziali_DeveDareFalseSeLaPasswordRisultaErrata(){
        String emailEsistente = "utenteTest@libero.it";
        String passwordErrata = "passwordTestErrata0@";
        assertFalse(gestioneUtente.verificaCredenzialiDaccesso(emailEsistente,passwordErrata));
    }
    @Test
    public void verificaCredenziali_DeveDareFalseSeLEmailRisultaErrata(){
        String emailSbagliata = "utenteTestSbagliata@libero.it";
        String passwordCorretta = "passwordTest0@";
        assertFalse(gestioneUtente.verificaCredenzialiDaccesso(emailSbagliata,passwordCorretta));
    }
    @Test
    public void verificaCredenziali_DeveDareFalseSeEntrambeLeCredenzialiRisultanoErrate(){
        String emailErrata= "utenteTest@libero.itErrata";
        String passwordErrata = "passwordTestErrata0@";
        assertFalse(gestioneUtente.verificaCredenzialiDaccesso(emailErrata,passwordErrata));
    }
    public void RegistraUtente_DeveDareFalseSeProviARegistraUnUtenteGiaRegistratoConQuelNomeUtente(){
        String usernameEsistente = "utenteTest";
        String emailNonEsistente = "utenteTestNonEsistente@libero.it";
        String passwordCorretta = "passwordTest0@";
        assertFalse(gestioneUtente.registraUtente(usernameEsistente,emailNonEsistente,passwordCorretta));
    }
    public void RegistraUtente_DeveDareFalseSeProviARegistraUnUtenteGiaRegistratoConQuellaEmail(){
        String usernameNonEsistente = "utenteTestNonRegistrato";
        String emailEsistente = "utenteTest@libero.it";
        String passwordCorretta = "passwordTest0@";
        assertFalse(gestioneUtente.registraUtente(usernameNonEsistente,emailEsistente,passwordCorretta));
    }
    public void RegistraUtente_DeveDareTrueSeProviARegistraUnUtenteConLeCredenzialiAccettabili(){
        String usernameNonEsistente = "utenteTestNonEsistente";
        String emailNonEsistente = "utenteTestNonEsistente@libero.it";
        String passwordCorretta = "passwordTest0@";
        assertTrue(gestioneUtente.registraUtente(usernameNonEsistente,emailNonEsistente,passwordCorretta));
    }
}



