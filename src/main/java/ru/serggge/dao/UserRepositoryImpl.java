package ru.serggge.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.serggge.entity.User;
import java.util.*;

public class UserRepositoryImpl implements UserRepository {

    private final CustomDataSource dataSource;

    public UserRepositoryImpl() {
        this.dataSource = new CustomDataSource();
    }

    @Override
    public User save(User user) {
        Session session = dataSource.getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        Session session = dataSource.getSession();
        Transaction transaction = session.beginTransaction();
        User user = session.find(User.class, userId);
        transaction.commit();
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        Transaction transaction = null;
        try {
            Session session = dataSource.getSession();
            transaction = session.beginTransaction();
            session.merge(user);
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
        try {
            Session session = dataSource.getSession();
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
        Session session = dataSource.getSession();
        Transaction transaction = session.beginTransaction();
        List<User> users = session.createQuery("from User", User.class)
                .list();
        transaction.commit();
        return users;
    }
}