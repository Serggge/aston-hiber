package ru.serggge.util;

import ru.serggge.command.*;
import ru.serggge.dao.Repository;
import ru.serggge.dao.UserRepository;
import ru.serggge.entity.User;
import java.util.HashMap;
import java.util.Map;

public class CommandSelector {

    private final Map<Button, Command> commands;

    public CommandSelector() {
        this.commands = initCommands();
    }

    public Command get(Button button) {
        return commands.get(button);
    }

    // Блок инициализации. Инстанцируем репозиторий и добавляем объекты команды
    private static Map<Button, Command> initCommands() {
        Repository<User> repository = new UserRepository();
        Map<Button, Command> commands = new HashMap<>();
        commands.put(Button.EXIT, new ExitCommand());
        commands.put(Button.CREATE, new CreateUserCommand(repository));
        commands.put(Button.UPDATE, new UpdateUserCommand(repository));
        commands.put(Button.FIND, new FindUserCommand(repository));
        commands.put(Button.DELETE, new DeleteUserCommand(repository));
        commands.put(Button.ALL, new FindAllCommand(repository));
        commands.put(Button.PRINT_MENU, new PrintMenuCommand());
        return commands;
    }
}
