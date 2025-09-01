package ru.serggge.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
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
        return null;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.empty();
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void deleteById(Long userId) {

    }

    private void init() {
        Properties properties = new Properties();
        properties.put(Environment.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        properties.put(Environment.JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5432/astonhiber");
        properties.put(Environment.JAKARTA_JDBC_USER, "root");
        properties.put(Environment.JAKARTA_JDBC_PASSWORD, "root");

        sessionFactory = new Configuration()
                .setProperties(properties)
                .configure()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
}
