package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.util.ConsoleReader;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class CreateUserCommand implements Command {

    private final UserRepository<User> repository;
    private final Logger log = Logger.getLogger(CreateUserCommand.class.getName());

    @Override
    public void execute() {
        User entity = createEntity();
        entity = repository.save(entity);
        log.info("User crated: " + entity);
    }

    private User createEntity() {
        return new User(name(), email(), age());
    }

    private String name() {
        // считываем из консоли имя сущности
        System.out.println("Enter name:");
        return ConsoleReader.readStringValue();
    }

    private String email() {
        // считываем из консоли почтовый адрес сущности
        System.out.println("Enter email:");
        return ConsoleReader.readStringValue();
    }

    private int age() {
        // считываем из консоли возраст сущности
        System.out.println("Enter age:");
        return ConsoleReader.readIntValue();
    }
}
