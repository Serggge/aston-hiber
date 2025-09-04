package ru.serggge.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.serggge.entity.User;
import java.util.*;

public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl() {
        this.sessionFactory = SessionFactoryHolder.getSessionFactory();
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
        User user;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            user = session.find(User.class, userId);
            transaction.commit();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            user = session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
        }
        return user;
    }

    @Override
    public void deleteById(Long userId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, userId);
            if (Objects.nonNull(user)) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Collection<User> findAll() {
        List<User> users;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            users = session.createQuery("from User", User.class)
                                         .list();
            transaction.commit();
        }
        return users;
    }
}