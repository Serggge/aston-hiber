package ru.serggge.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ru.serggge.entity.User;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class UserUserRepositoryImpl implements UserRepository {

    private final EntityManagerFactory emf;

    public UserUserRepositoryImpl() {
        this.emf = DataSourceFactory.forEntityClass(User.class);
    }

    @Override
    public User save(User user) {
        transactional(entityManager -> {
            entityManager.persist(user);
        });
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        User user = transactionalWithResult(entityManager ->
                entityManager.find(User.class, userId));
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findActiveUser(Long userId) {
        User user = transactionalWithResult(entityManager -> {
            TypedQuery<User> query = entityManager.createQuery("""
                    SELECT u
                    FROM User u
                    WHERE u.id=:userId AND u.isActive=TRUE
                    """, User.class);
            query.setParameter("userId", userId);
            try {
                return query.getSingleResult();
            } catch (RuntimeException e) {
                return null;
            }
        });
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        return transactionalWithResult(entityManager ->
                entityManager.merge(user));
    }

    @Override
    public void deleteById(Long userId) {
        transactional(entityManager -> {
            User user = entityManager.find(User.class, userId);
            if (user != null) {
                entityManager.remove(user);
            }
        });
    }

    @Override
    public void disableUser(Long userId) {
        transactional(entityManager -> {
            User user = entityManager.find(User.class, userId);
            user.setIsActive(false);
            entityManager.merge(user);
        });
    }

    @Override
    public Collection<User> findAll() {
        return transactionalWithResult(entityManager -> {
            TypedQuery<User> query = entityManager.createQuery("from User", User.class);
            return query.getResultList();
        });
    }

    private void transactional(Consumer<EntityManager> action) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = emf.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private <T> T transactionalWithResult(Function<EntityManager, T> action) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = emf.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            T result = action.apply(entityManager);
            transaction.commit();
            return result;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}