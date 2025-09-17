package ru.serggge.dao;

import ru.serggge.entity.User;
import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User save(User entity);

    Optional<User> findById(Long userId);

    Optional<User> findActiveUser(Long userId);

    User update(User entity);

    @Deprecated
    void deleteById(Long userId);

    void disableUser(Long userId);

    Collection<User> findAll();
}