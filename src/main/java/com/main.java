package com;
import com.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.util.HibernateUtil;

public class main {
    public static void main(String[] args) {
        // Create a new user object
        User user = new User();
        user.setUsername("JohnDoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        // Open Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        // Save the object
        session.save(user);

        // Commit transaction and close session
        transaction.commit();
        session.close();

        System.out.println("User saved!");
    }
}