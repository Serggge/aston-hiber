package ru.serggge.dao;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T> {

    T save(T entity);

    Optional<T> findById(Long id);

    T update(T entity);

    void deleteById(Long id);

    Collection<T> findAll();
}
