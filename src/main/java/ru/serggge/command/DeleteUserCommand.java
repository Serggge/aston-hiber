package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.util.ScannerHolder;

@RequiredArgsConstructor
public class DeleteUserCommand implements Command {

    private final UserRepository userRepository;

    @Override
    public void execute() {
        // считываем из консоли айди сущности для удаления из БД
        System.out.println("Enter user ID:");
        long userId = ScannerHolder.readLongValue();
        userRepository.deleteById(userId);
        System.out.println("User deleted");
    }
}