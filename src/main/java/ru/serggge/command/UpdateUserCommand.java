package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.interceptors.ExceptionHandling;
import ru.serggge.util.OperationReader;
import java.time.Instant;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class UpdateUserCommand implements Command {

    private final UserRepository userRepository;
    private final Logger log = Logger.getLogger(UpdateUserCommand.class.getName());

    @ExceptionHandling
    @Override
    public void execute() {
        User entity = createEntity();
        entity = userRepository.update(entity);
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