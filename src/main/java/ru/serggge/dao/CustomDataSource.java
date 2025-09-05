package ru.serggge.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import ru.serggge.entity.User;
import java.util.Properties;

public class CustomDataSource {

    private final SessionFactory sessionFactory;

    public CustomDataSource() {
        Properties properties = new Properties();
        properties.put(Environment.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        properties.put(Environment.JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:6543/astonhiberdb");
        properties.put(Environment.JAKARTA_JDBC_USER, "root");
        properties.put(Environment.JAKARTA_JDBC_PASSWORD, "root");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(Environment.HBM2DDL_AUTO, "update");

        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

    public Session getSession() {
        try (Session session = sessionFactory.openSession()) {
            return session;
        }
    }
}