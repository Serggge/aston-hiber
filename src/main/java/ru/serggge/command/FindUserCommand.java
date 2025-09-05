package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.interceptors.ExceptionHandling;
import ru.serggge.util.OperationReader;

@RequiredArgsConstructor
public class FindUserCommand implements Command {

    private final UserRepository userRepository;

    @ExceptionHandling
    @Override
    public void execute() {
        System.out.println("Enter user ID:");
        // считываем из консоли айди сущности для поиска в БД
        Long userId = OperationReader.readLongValue();
        userRepository.findById(userId)
                      .ifPresentOrElse(System.out::println,
                              () -> System.out.println("User not found"));
    }
}