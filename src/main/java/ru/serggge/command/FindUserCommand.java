package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.util.ConsoleReader;
import java.util.Optional;

@RequiredArgsConstructor
public class FindUserCommand implements Command {

    private final UserRepository repository;
    private final ConsoleReader consoleReader;

    @Override
    public void execute() {
        System.out.println("Enter user ID:");
        // считываем из консоли айди сущности для поиска в БД
        Long userId = consoleReader.readLongValue();
        System.out.println("Search for inactive users? (Y/N)");
        // опция поиска пользователей только активных или всех ранее добавленных в БД
        boolean isConfirmed = consoleReader.readBooleanValue();
        Optional<User> user;
        if (isConfirmed) {
            user = findHiddenAndActive(userId);
        } else {
            user = findActiveUser(userId);
        }
        user.ifPresentOrElse(System.out::println,
                () -> System.out.println("User not found"));
    }

    private Optional<User> findActiveUser(Long userId) {
        return repository.findActiveUser(userId);
    }

    private Optional<User> findHiddenAndActive(Long userId) {
        return repository.findById(userId);
    }
}