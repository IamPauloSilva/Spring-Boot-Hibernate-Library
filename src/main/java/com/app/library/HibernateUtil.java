package com.app.library;

import com.app.library.Model.Book;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Book.class).buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }



    public static void shutdown() {
        getSessionFactory().close();
    }
}
