package ru.serggge.util;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import ru.serggge.command.*;
import ru.serggge.config.Profile;
import ru.serggge.dao.DataSourceFactory;
import ru.serggge.dao.UserRepository;
import ru.serggge.dao.UserUserRepositoryImpl;
import ru.serggge.entity.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CommandHolder {

    private final Map<Button, Command> commands;
    private final ConsoleReader consoleReader;

    public CommandHolder(Profile profile) {
        this.commands = initCommands(profile);
        this.consoleReader = new ConsoleReader();
    }

    // конструктор для testcontainers с динамическими property
    public CommandHolder(Properties properties, ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
        this.commands = initCommandsForTest(properties);
    }

    public Command get(Button button) {
        return commands.get(button);
    }

    // Блок инициализации. Инстанцируем репозиторий и добавляем объекты команды для каждого CRUDa
    private Map<Button, Command> initCommands(Profile profile) {
        EntityManagerFactory emf = DataSourceFactory.forEntityClass(User.class, profile);
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

    private Map<Button, Command> initCommandsForTest(Properties properties) {
        EntityManagerFactory emf = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        UserRepository repository = new UserUserRepositoryImpl(emf);

        Map<Button, Command> commands = new HashMap<>();
        commands.put(Button.CREATE, new CreateUserCommand(repository, consoleReader));
        commands.put(Button.UPDATE, new UpdateUserCommand(repository, consoleReader));
        commands.put(Button.FIND, new FindUserCommand(repository, consoleReader));
        commands.put(Button.DELETE, new DeleteUserCommand(repository, consoleReader));
        commands.put(Button.ALL, new FindAllCommand(repository));
        return commands;
    }
}