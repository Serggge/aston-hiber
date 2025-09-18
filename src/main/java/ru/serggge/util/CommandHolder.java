package ru.serggge.util;

import jakarta.persistence.EntityManagerFactory;
import ru.serggge.command.*;
import ru.serggge.config.Profile;
import ru.serggge.dao.DataSourceFactory;
import ru.serggge.dao.UserRepository;
import ru.serggge.dao.UserUserRepositoryImpl;
import ru.serggge.entity.User;
import java.util.HashMap;
import java.util.Map;

public class CommandHolder {

    private final Map<Button, Command> commands;

    public CommandHolder(Profile profile) {
        this.commands = initCommands(profile);
    }

    public Command get(Button button) {
        return commands.get(button);
    }

    // Блок инициализации. Инстанцируем репозиторий и добавляем объекты команды для каждого CRUDa
    private static Map<Button, Command> initCommands(Profile profile) {
        EntityManagerFactory emf = DataSourceFactory.forEntityClass(User.class, profile);
        ConsoleReader consoleReader = new ConsoleReader();
        UserRepository repository = new UserUserRepositoryImpl(emf);

        Map<Button, Command> commands = new HashMap<>();
        commands.put(Button.EXIT, new ExitCommand());
        commands.put(Button.CREATE, new CreateUserCommand(repository, consoleReader));
        commands.put(Button.UPDATE, new UpdateUserCommand(repository, consoleReader));
        commands.put(Button.FIND, new FindUserCommand(repository, consoleReader));
        commands.put(Button.DELETE, new DeleteUserCommand(repository, consoleReader));
        commands.put(Button.ALL, new FindAllCommand(repository));
        commands.put(Button.PRINT_MENU, new PrintMenuCommand());
        return commands;
    }
}
