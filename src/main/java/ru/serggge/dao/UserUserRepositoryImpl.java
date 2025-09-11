package ru.serggge.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import ru.serggge.annotations.UnitName;
import ru.serggge.entity.User;
import java.util.*;
import java.util.function.Consumer;

public class UserUserRepositoryImpl implements UserRepository<User> {

    private final EntityManagerFactory emf;

    public UserUserRepositoryImpl() {
        // unitName берём в аннотации над классом сущности
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
    public Optional<User> findByIdIgnoreActivity(Long userId) {
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
    public Optional<User> findById(Long userId) {
        User user;
        try (EntityManager entityManager = emf.createEntityManager()) {
            user = entityManager.createQuery("SELECT u FROM User u where u.id=:userId AND u.isActive=TRUE", User.class)
                                          .setParameter("userId", userId)
                                          .getSingleResult();
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
    public void deactivateUser(Long userId) {
        executeInsideTransaction(entityManager -> {
            User user = entityManager.find(User.class, userId);
            user.setIsActive(false);
            entityManager.merge(user);
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