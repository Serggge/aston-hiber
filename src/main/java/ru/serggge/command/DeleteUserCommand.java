package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.Repository;
import ru.serggge.entity.User;
import ru.serggge.util.OperationReader;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class DeleteUserCommand implements Command {

    private final Repository<User> repository;
    private final Logger log = Logger.getLogger(DeleteUserCommand.class.getName());

    @Override
    public void execute() {
        // считываем из консоли айди сущности для удаления из БД
        System.out.println("Enter user ID:");
        long userId = OperationReader.readLongValue();
        repository.deleteById(userId);
        log.info("User deleted");
    }
}