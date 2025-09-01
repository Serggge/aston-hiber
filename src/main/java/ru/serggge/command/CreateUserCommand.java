package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.util.ScannerHolder;
import java.time.Instant;

@RequiredArgsConstructor
public class CreateUserCommand implements Command {

    private final UserRepository userRepository;

    @Override
    public void execute() {
        User entity = createEntity();
        entity = userRepository.save(entity);
        System.out.println("User crated: " + entity);
    }

    private User createEntity() {
        return new User(null, name(), email(), age(), Instant.now());
    }

    private String name() {
        // считываем из консоли имя сущности
        System.out.println("Enter name:");
        return ScannerHolder.readStringValue();
    }

    private String email() {
        // считываем из консоли емаил сущности
        System.out.println("Enter email:");
        return ScannerHolder.readStringValue();
    }

    private int age() {
        // считываем из консоли возраст сущности
        System.out.println("Enter age:");
        return ScannerHolder.readIntValue();
    }
}
