package ru.serggge.util;

import ru.serggge.command.*;
import ru.serggge.dao.UserRepository;
import ru.serggge.dao.UserRepositoryImpl;
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

    // Блок инициализации. Инстанцируем репозиторий и добавляем команды
    private static Map<Button, Command> initCommands() {
        UserRepository userRepository = new UserRepositoryImpl();
        Map<Button, Command> commands = new HashMap<>();
        commands.put(Button.EXIT, new ExitCommand());
        commands.put(Button.CREATE, new CreateUserCommand(userRepository));
        commands.put(Button.UPDATE, new UpdateUserCommand(userRepository));
        commands.put(Button.FIND, new FindUserCommand(userRepository));
        commands.put(Button.DELETE, new DeleteUserCommand(userRepository));
        commands.put(Button.ALL, new FindAllCommand(userRepository));
        commands.put(Button.PRINT_MENU, new PrintMenuCommand());
        commands.put(Button.INVALID_OPERATION, new InvalidOperationCommand());
        return commands;
    }
}
