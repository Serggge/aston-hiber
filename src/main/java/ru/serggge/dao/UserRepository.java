package ru.serggge.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import ru.serggge.entity.User;
import java.util.*;
import java.util.function.Consumer;

public class UserRepository implements Repository<User> {

    private final EntityManager entityManager;

    public UserRepository() {
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("user-persistence")) {
            this.entityManager = emf.createEntityManager();
        }
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        entityManager.detach(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        User user = entityManager.find(User.class, userId);
        if (Objects.nonNull(user)) {
            entityManager.detach(user);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        executeInsideTransaction(entityManager -> entityManager.merge(user));
        return user;
    }

    @Override
    public void deleteById(Long userId) {
        executeInsideTransaction(entityManager -> {
            User user = entityManager.find(User.class, userId);
            if (Objects.nonNull(user)) {
                entityManager.remove(user);
            }
        });
    }

    @Override
    public Collection<User> findAll() {
        return entityManager.createQuery("from User", User.class)
                            .getResultList();
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(entityManager);
            tx.commit();
        }
        catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }
}