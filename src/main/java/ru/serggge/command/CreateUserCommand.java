package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.interceptors.ExceptionHandling;
import ru.serggge.util.OperationReader;
import java.time.Instant;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class CreateUserCommand implements Command {

    private final UserRepository userRepository;
    private final Logger log = Logger.getLogger(CreateUserCommand.class.getName());

    @ExceptionHandling
    @Override
    public void execute() {
        User entity = createEntity();
        entity = userRepository.save(entity);
        log.info("User crated: " + entity);
    }

    private User createEntity() {
        return new User(null, name(), email(), age(), Instant.now());
    }

    private String name() {
        // считываем из консоли имя сущности
        System.out.println("Enter name:");
        return OperationReader.readStringValue();
    }

    private String email() {
        // считываем из консоли емаил сущности
        System.out.println("Enter email:");
        return OperationReader.readStringValue();
    }

    private int age() {
        // считываем из консоли возраст сущности
        System.out.println("Enter age:");
        return OperationReader.readIntValue();
    }
}
