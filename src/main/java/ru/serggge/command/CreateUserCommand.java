package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.util.ConsoleReader;

@RequiredArgsConstructor
@Slf4j
public class CreateUserCommand implements Command {

    private final UserRepository<User> repository;

    @Override
    public void execute() {
        User entity = createEntity();
        entity = repository.save(entity);
        log.info("User created: " + entity);
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
