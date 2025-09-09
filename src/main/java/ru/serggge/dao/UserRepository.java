package ru.serggge.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import ru.serggge.annotations.UnitName;
import ru.serggge.entity.User;
import java.util.*;
import java.util.function.Consumer;

public class UserRepository implements Repository<User> {

    private final EntityManagerFactory emf;

    public UserRepository() {
        String unitName = readUnitName();
        this.emf = DataSourceFactory.fromUnitName(unitName);
    }

    @Override
    public User save(User user) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            entityManager.persist(user);
            entityManager.detach(user);
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        User user;
        try (EntityManager entityManager = emf.createEntityManager()) {
            user = entityManager.find(User.class, userId);
            if (Objects.nonNull(user)) {
                entityManager.detach(user);
            }
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
        try (EntityManager entityManager = emf.createEntityManager()) {
            return entityManager.createQuery("from User", User.class)
                                .getResultList();
        }
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = emf.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
                throw e;
            }
        }
    }

    private String readUnitName() {
        UnitName annotation = User.class.getAnnotation(UnitName.class);
        if (annotation != null) {
            return annotation.name();
        } else {
            throw new IllegalStateException("Must declare unit name in the entity class");
        }
    }
}