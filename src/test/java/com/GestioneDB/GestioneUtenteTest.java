package com.GestioneDB;

import com.entity.User;
import com.gallery.gui.AlertInfo;
import com.util.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestioneUtenteTest {

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Transaction transactionMock;

    @Mock
    private Transaction transactionMockVerifica;

    @InjectMocks
    private GestioneUtente gestioneUtente;

    @BeforeAll
    static void disableAlerts() {
        // Evita di creare veri popup JavaFX durante i test
        AlertInfo.DISABLE_ALERTS_FOR_TESTS = true;
    }

    @BeforeEach
    void setUp() {
        // Iniettiamo la sessionFactory mock
        gestioneUtente = new GestioneUtente(sessionFactoryMock);

        // Stub base: sessionFactoryMock.openSession() => sessionMock
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        // e sessionMock.beginTransaction() => transactionMock
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testRegistraUtente_GiaEsisteUsername() {
        // Mock della sessione e della transazione
        when(sessionMock.beginTransaction()).thenReturn(transactionMockVerifica);

        // Mock per la query di verifica username
        Query<User> queryMockUsername = mock(Query.class);
        when(sessionMock.createQuery("FROM User u WHERE u.username = :username", User.class))
                .thenReturn(queryMockUsername);
        when(queryMockUsername.setParameter(eq("username"), anyString()))
                .thenReturn(queryMockUsername);
        when(queryMockUsername.uniqueResult())
                .thenReturn(new User("esistente", "old@test.it", "pwd"));

        // Esecuzione del metodo sotto test
        boolean result = gestioneUtente.registraUtente("esistente", "nuovo@test.it", "password123");
        assertFalse(result, "Se l'username esiste, deve tornare false");

        // Verifica che 'persist' non venga chiamato
        verify(sessionMock, never()).persist(any(User.class));

        // Verifica che 'commit' sia stato chiamato sulla transazione di verifica
        verify(transactionMockVerifica, times(1)).commit();

        // Verifica che 'rollback' non venga chiamato
        verify(transactionMockVerifica, never()).rollback();
    }

    @Test
    void testRegistraUtente_GiaEsisteEmail() {
        // Mock delle transazioni
        Transaction transactionMockVerificaUsername = mock(Transaction.class);
        Transaction transactionMockVerificaEmail = mock(Transaction.class);

        // Configura sessione per aprire diverse transazioni
        when(sessionMock.beginTransaction())
                .thenReturn(transactionMockVerificaUsername)
                .thenReturn(transactionMockVerificaEmail);

        // Mock per la query di verifica username
        Query<User> queryMockUsername = mock(Query.class);
        when(sessionMock.createQuery("FROM User u WHERE u.username = :username", User.class))
                .thenReturn(queryMockUsername);
        when(queryMockUsername.setParameter(eq("username"), anyString()))
                .thenReturn(queryMockUsername);
        when(queryMockUsername.uniqueResult())
                .thenReturn(null); // Username non esiste

        // Mock per la query di verifica email
        Query<User> queryMockEmail = mock(Query.class);
        when(sessionMock.createQuery("FROM User u WHERE u.email = :email", User.class))
                .thenReturn(queryMockEmail);
        when(queryMockEmail.setParameter(eq("email"), anyString()))
                .thenReturn(queryMockEmail);
        when(queryMockEmail.uniqueResult())
                .thenReturn(new User("someone", "esistente@test.it", "pwd")); // Email esistente

        // Esecuzione del metodo sotto test
        boolean result = gestioneUtente.registraUtente("nuovoUsername", "esistente@test.it", "password123");
        assertFalse(result, "Se l'email esiste, deve tornare false");

        // Verifica che 'persist' non venga chiamato
        verify(sessionMock, never()).persist(any(User.class));

        // Verifica che 'commit' sia stato chiamato separatamente per ogni verifica
        verify(transactionMockVerificaUsername, times(1)).commit();
        verify(transactionMockVerificaEmail, times(1)).commit();

        // Verifica che 'rollback' non venga chiamato
        verify(transactionMockVerificaUsername, never()).rollback();
        verify(transactionMockVerificaEmail, never()).rollback();
    }




    @Test
    void testVerificaCredenzialiDaccesso_UtenteNonEsiste() {
        Query<User> queryMock = mock(Query.class);

        when(sessionMock.createQuery("FROM User u WHERE u.email = :email", User.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("email"), anyString()))
                .thenReturn(queryMock);
        when(queryMock.uniqueResult())
                .thenReturn(null);

        boolean result = gestioneUtente.verificaCredenzialiDaccesso("nonEsiste@test.it", "abc");
        assertFalse(result, "Se l'utente non c'è, deve restituire false");
    }

    @Test
    void testVerificaCredenzialiDaccesso_UtenteEsistePasswordCorretta() {
        Query<User> queryMock = mock(Query.class);
        // supponiamo password hashed
        String hashedPass = PasswordUtil.hashPassword("password123");
        User userInDb = new User("username", "mail@test.it", hashedPass);

        when(sessionMock.createQuery("FROM User u WHERE u.email = :email", User.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("email"), anyString()))
                .thenReturn(queryMock);
        when(queryMock.uniqueResult())
                .thenReturn(userInDb);

        // Verifichiamo
        boolean result = gestioneUtente.verificaCredenzialiDaccesso("mail@test.it", "password123");
        assertTrue(result, "Se la password coincide, true");
    }

    @Test
    void testGetUserByEmail_Trovato() {
        Query<User> queryMock = mock(Query.class);
        User userMock = new User("testUser", "test@test.it", "hashed");

        when(sessionMock.createQuery("FROM User u WHERE u.email = :email", User.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("email"), anyString()))
                .thenReturn(queryMock);
        when(queryMock.uniqueResult())
                .thenReturn(userMock);

        User result = gestioneUtente.getUserByEmail("test@test.it");
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testGetUserByEmail_NonTrovato() {
        Query<User> queryMock = mock(Query.class);

        when(sessionMock.createQuery("FROM User u WHERE u.email = :email", User.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("email"), anyString()))
                .thenReturn(queryMock);
        when(queryMock.uniqueResult())
                .thenReturn(null);

        User result = gestioneUtente.getUserByEmail("nonEsiste@test.it");
        assertNull(result);
    }

    @Test
    void testVerificaSeUnUtenteEsiste() {
        // la logica controlla prima username, poi email
        Query<User> queryMockUsername = mock(Query.class);
        Query<User> queryMockEmail = mock(Query.class);

        // username esiste
        when(sessionMock.createQuery("FROM User u WHERE u.username = :username", User.class))
                .thenReturn(queryMockUsername);
        when(queryMockUsername.setParameter(eq("username"), anyString()))
                .thenReturn(queryMockUsername);
        when(queryMockUsername.uniqueResult())
                .thenReturn(new User("esistente", "ex@test.it", "pass"));

        // email query (teoricamente non dovrebbe servire se già trova username)
        when(sessionMock.createQuery("FROM User u WHERE u.email = :email", User.class))
                .thenReturn(queryMockEmail);
        when(queryMockEmail.setParameter(eq("email"), anyString()))
                .thenReturn(queryMockEmail);
        when(queryMockEmail.uniqueResult())
                .thenReturn(null);

        boolean result = gestioneUtente.verificaSeUnUtenteEsiste("esistente", "any@test.it");
        assertTrue(result, "Se username c'è, ritorna true");
    }

    @Test
    void testVerificaSeUnUtenteEsisteByEmail() {
        Query<User> queryMock = mock(Query.class);
        when(sessionMock.createQuery("FROM User u WHERE u.email = :email", User.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("email"), anyString()))
                .thenReturn(queryMock);

        // Caso: esiste
        when(queryMock.uniqueResult()).thenReturn(new User("usr", "esiste@test.it", "pass"));
        assertTrue(gestioneUtente.verificaSeUnUtenteEsisteByEmail("esiste@test.it"));

        // Caso: non esiste
        when(queryMock.uniqueResult()).thenReturn(null);
        assertFalse(gestioneUtente.verificaSeUnUtenteEsisteByEmail("nonEsiste@test.it"));
    }

    @Test
    void testVerificaSeUnUtenteEsisteByUsername() {
        Query<User> queryMock = mock(Query.class);
        when(sessionMock.createQuery("FROM User u WHERE u.username = :username", User.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("username"), anyString()))
                .thenReturn(queryMock);

        // Caso: esiste
        when(queryMock.uniqueResult()).thenReturn(new User("oldUser", "xxx@xxx.it", "pass"));
        assertTrue(gestioneUtente.verificaSeUnUtenteEsisteByUsername("oldUser"));

        // Caso: non esiste
        when(queryMock.uniqueResult()).thenReturn(null);
        assertFalse(gestioneUtente.verificaSeUnUtenteEsisteByUsername("nuovoUser"));
    }
}
