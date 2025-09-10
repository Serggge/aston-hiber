package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;

@RequiredArgsConstructor
public class FindAllCommand implements Command {

    private final UserRepository<User> repository;

    @Override
    public void execute() {
        repository.findAll()
                  .forEach(System.out::println);
    }
}