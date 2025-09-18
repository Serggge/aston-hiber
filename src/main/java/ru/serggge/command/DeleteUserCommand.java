package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.serggge.dao.UserRepository;
import ru.serggge.util.ConsoleReader;

@RequiredArgsConstructor
@Slf4j
public class DeleteUserCommand implements Command {

    private final UserRepository repository;
    private final ConsoleReader consoleReader;

    @Override
    public void execute() {
        System.out.println("Enter user ID:");
        long userId = consoleReader.readLongValue();
        System.out.println("Delete user permanently? (Y/N)");
        boolean isConfirmed = consoleReader.readBooleanValue();
        if (isConfirmed) {
            eraseFromDatabase(userId);
        } else {
            setInactive(userId);
        }
    }

    private void eraseFromDatabase(Long userId) {
        repository.deleteById(userId);
        log.info("User deleted");
    }

    private void setInactive(Long userId) {
        repository.disableUser(userId);
        log.info("User deactivated");
    }
}