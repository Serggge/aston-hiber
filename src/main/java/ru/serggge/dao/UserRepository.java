package ru.serggge.dao;

import ru.serggge.entity.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long userId);

    User update(User user);

    void deleteById(Long userId);
}
