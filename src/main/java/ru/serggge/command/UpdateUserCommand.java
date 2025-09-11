package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.util.ConsoleReader;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class UpdateUserCommand implements Command {

    private final UserRepository<User> repository;
    private final Logger log = Logger.getLogger(UpdateUserCommand.class.getName());

    @Override
    public void execute() {
        User entity = createUpdatedEntity();
        entity = repository.update(entity);
        log.info("User updated: " + entity);
    }

    private User createUpdatedEntity() {
        User user = new User(name(), email(), age());
        user.setId(userId());
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