package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.Repository;
import ru.serggge.entity.User;
import ru.serggge.util.OperationReader;

@RequiredArgsConstructor
public class FindUserCommand implements Command {

    private final Repository<User> repository;

    @Override
    public void execute() {
        System.out.println("Enter user ID:");
        // считываем из консоли айди сущности для поиска в БД
        Long userId = OperationReader.readLongValue();
        repository.findById(userId)
                  .ifPresentOrElse(System.out::println,
                              () -> System.out.println("User not found"));
    }
}