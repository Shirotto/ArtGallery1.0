/*package com;
import com.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.util.HibernateUtil;

public class main {
    public static void main(String[] args) {
        // Obtain a session from HibernateUtil
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Create and save the first user
            User user1 = new User();
            user1.setUsername("John");
            user1.setEmail("john.doe@ciao.com");
            user1.setPassword("1234");
            session.save(user1);

            // Create and save the second user
            User user2 = new User();
            user2.setUsername("Jane");
            user2.setEmail("jane.doe@eciao.com");
            user2.setPassword("1234");
            session.save(user2);



            User user3 = new User();
            user3.setUsername("JaneDun");
            user3.setEmail("jane.dun@eciao.com");
            user3.setPassword("1234");
            session.save(user3);





            // Commit the transaction
            transaction.commit();
            System.out.println("Users saved successfully!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        // Shutdown Hibernate
        HibernateUtil.shutdown();
    }}*/