package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
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
        return nextLine();
    }

    private String email() {
        // считываем из консоли емаил сущности
        System.out.println("Enter email:");
        return nextLine();
    }

    private int age() {
        // считываем из консоли возраст сущности, не принимаем значения меньше 0
        System.out.println("Enter age:");
        int age = -1;
        while (age < 0) {
            age = nextInt();
            if (age < 0) {
                System.out.println("Age can't be negative");
            }
        }
        return age;
    }

}
