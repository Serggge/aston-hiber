package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;

@RequiredArgsConstructor
public class DeleteUserCommand implements Command {

    private final UserRepository userRepository;

    @Override
    public void execute() {
        // считываем из консоли айди сущности для удаления из БД
        long userId = nextLong();
        userRepository.deleteById(userId);
        System.out.println("User deleted");
    }
}