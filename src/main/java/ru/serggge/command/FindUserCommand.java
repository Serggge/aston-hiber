package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;

@RequiredArgsConstructor
public class FindUserCommand implements Command {

    private final UserRepository userRepository;

    @Override
    public void execute() {
        // считываем из консоли айди сущности для поиска в БД
        long userId = nextLong();
        userRepository.findById(userId)
                      .ifPresentOrElse(System.out::println,
                              () -> System.out.println("User not found"));
    }
}