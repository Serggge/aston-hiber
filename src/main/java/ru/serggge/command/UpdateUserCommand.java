package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import ru.serggge.util.ScannerHolder;

import java.time.Instant;

@RequiredArgsConstructor
public class UpdateUserCommand implements Command {

    private final UserRepository userRepository;

    @Override
    public void execute() {
        User entity = createEntity();
        entity = userRepository.update(entity);
        System.out.println("User updated: " + entity);
    }

    private User createEntity() {
        return new User(userId(), name(), email(), age(), Instant.now());
    }

    private Long userId() {
        System.out.println("Enter user id: ");
        return ScannerHolder.readLongValue();
    }

    private String name() {
        System.out.println("Enter new name:");
        return ScannerHolder.readStringValue();
    }

    private String email() {
        System.out.println("Enter new email:");
        return ScannerHolder.readStringValue();
    }

    private int age() {
        System.out.println("Enter new age:");
        return ScannerHolder.readIntValue();
    }
}