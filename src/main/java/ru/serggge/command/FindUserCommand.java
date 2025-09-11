package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.util.ConsoleReader;

import java.util.Optional;

@RequiredArgsConstructor
public class FindUserCommand implements Command {

    private final UserRepository<User> repository;

    @Override
    public void execute() {
        System.out.println("Enter user ID:");
        // считываем из консоли айди сущности для поиска в БД
        Long userId = ConsoleReader.readLongValue();
        System.out.println("Search for inactive users? (Y/N)");
        // опция поиска пользователей только активных или всех ранее добавленных в БД
        String showHidden = ConsoleReader.readStringValue();
        selectSearchMode(userId, showHidden)
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("User not found"));
    }

    private Optional<User> selectSearchMode(Long userId, String showHidden) {
        return switch (showHidden.toUpperCase()) {
            case "Y" -> findHiddenAndActive(userId);
            case "N" -> findActiveUser(userId);
            default -> throw new IllegalStateException("Unexpected value: " + showHidden.toUpperCase());
        };
    }

    private Optional<User> findActiveUser(Long userId) {
        return repository.findById(userId);
    }

    private Optional<User> findHiddenAndActive(Long userId) {
        return repository.findByIdIgnoreActivity(userId);
    }
}