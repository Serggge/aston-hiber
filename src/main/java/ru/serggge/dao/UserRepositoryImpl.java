package ru.serggge.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import ru.serggge.entity.User;
import java.util.Optional;
import java.util.Properties;

public class UserRepositoryImpl implements UserRepository {

    private SessionFactory sessionFactory;

    public UserRepositoryImpl() {
        init();
    }

    @Override
    public User save(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<User> namedQuery = session.createNamedQuery("findById", User.class)
                                            .setParameter("id", userId);
            User user = namedQuery.getSingleResultOrNull();
            transaction.commit();
            return Optional.ofNullable(user);
        }
    }

    @Override
    public User update(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            user = session.merge(user);
            transaction.commit();
        }
        return user;
    }

    @Override
    public void deleteById(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<User> namedQuery = session.createNamedQuery("deleteById", User.class)
                                            .setParameter("id", userId);
            namedQuery.executeUpdate();
            transaction.commit();
            session.flush();
        }
    }

    private void init() {
        Properties properties = new Properties();
        properties.put(Environment.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        properties.put(Environment.JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5432/astonhiber");
        properties.put(Environment.JAKARTA_JDBC_USER, "root");
        properties.put(Environment.JAKARTA_JDBC_PASSWORD, "root");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(Environment.HBM2DDL_AUTO, "update");

        sessionFactory = new Configuration()
                .setProperties(properties)
                .configure()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
}