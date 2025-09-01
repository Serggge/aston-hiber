package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;

import java.util.InputMismatchException;

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
        return new User(userId(), name(), email(), age(), null);
    }

    private Long userId() {
        System.out.println("Enter user id: ");
        return nextLong();
    }

    private String name() {
        System.out.println("Enter new name or press Enter to skip:");
        String newName = nextLine();
        return newName.isBlank() ? null : newName;
    }

    private String email() {
        System.out.println("Enter new email or press Enter to skip:");
        String newEmail = nextLine();
        return newEmail.isBlank() ? null : newEmail;
    }

    private int age() {
        System.out.println("Enter new age or press Enter to skip:");
        String userInput = nextLine();
        try {
            int newAge = Integer.parseInt(userInput);
            return newAge > 0 ? newAge : -1;
        } catch (InputMismatchException | NumberFormatException e) {
            return -1;
        }
    }
}