package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;

@RequiredArgsConstructor
public class FindAllCommand implements Command {

    private final UserRepository repository;

    @Override
    public void execute() {
        repository.findAll()
                  .forEach(System.out::println);
    }
}