package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;
import ru.serggge.interceptors.ExceptionHandling;
import ru.serggge.util.OperationReader;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class DeleteUserCommand implements Command {

    private final UserRepository userRepository;
    private final Logger log = Logger.getLogger(DeleteUserCommand.class.getName());

    @ExceptionHandling
    @Override
    public void execute() {
        // считываем из консоли айди сущности для удаления из БД
        System.out.println("Enter user ID:");
        long userId = OperationReader.readLongValue();
        userRepository.deleteById(userId);
        log.info("User deleted");
    }
}