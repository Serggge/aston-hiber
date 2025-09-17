package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.util.ConsoleReader;

@RequiredArgsConstructor
@Slf4j
public class UpdateUserCommand implements Command {

    private final UserRepository repository;

    @Override
    public void execute() {
        User entity = createUpdatedEntity();
        entity = repository.update(entity);
        log.info("User updated: {}", entity);
    }

    private User createUpdatedEntity() {
        Long userId = userId();
        User user = new User(name(), email(), age());
        user.setId(userId);
        return user;
    }

    private Long userId() {
        System.out.println("Enter user id: ");
        return ConsoleReader.readLongValue();
    }

    private String name() {
        System.out.println("Enter new name:");
        return ConsoleReader.readStringValue();
    }

    private String email() {
        System.out.println("Enter new email:");
        return ConsoleReader.readStringValue();
    }

    private Integer age() {
        System.out.println("Enter new age:");
        return ConsoleReader.readIntValue();
    }
}