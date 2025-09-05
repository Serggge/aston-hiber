package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.Repository;
import ru.serggge.entity.User;

@RequiredArgsConstructor
public class FindAllCommand implements Command {

    private final Repository<User> repository;

    @Override
    public void execute() {
        repository.findAll()
                  .forEach(System.out::println);
    }
}