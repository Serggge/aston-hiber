package ru.serggge.dao;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository<T> {

    T save(T entity);

    Optional<T> findByIdIgnoreActivity(Long userId);

    Optional<T> findById(Long userId);

    T update(T entity);

    @Deprecated
    void deleteById(Long userId);

    void deactivateUser(Long userId);

    Collection<T> findAll();
}