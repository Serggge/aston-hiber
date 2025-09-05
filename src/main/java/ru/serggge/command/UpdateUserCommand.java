package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.Repository;
import ru.serggge.entity.User;
import ru.serggge.util.OperationReader;
import java.time.Instant;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class UpdateUserCommand implements Command {

    private final Repository<User> repository;
    private final Logger log = Logger.getLogger(UpdateUserCommand.class.getName());

    @Override
    public void execute() {
        User entity = createEntity();
        entity = repository.update(entity);
        log.info("User updated: " + entity);
    }

    private User createEntity() {
        return new User(userId(), name(), email(), age(), Instant.now());
    }

    private Long userId() {
        System.out.println("Enter user id: ");
        return OperationReader.readLongValue();
    }

    private String name() {
        System.out.println("Enter new name:");
        return OperationReader.readStringValue();
    }

    private String email() {
        System.out.println("Enter new email:");
        return OperationReader.readStringValue();
    }

    private int age() {
        System.out.println("Enter new age:");
        return OperationReader.readIntValue();
    }
}