package ru.serggge;

import ru.serggge.command.*;
import ru.serggge.dao.UserRepository;
import ru.serggge.dao.UserRepositoryImpl;
import ru.serggge.interceptors.ExceptionHandling;
import ru.serggge.util.ScannerHolder;
import java.util.*;
import java.util.logging.Logger;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        // инициализация репозитория и команд
        Map<Button, Command> commands = initCommands();
        // выполняем в бесконечном цикле, пока пользователь не выберет "Выход"
        while (true) {
            printMenu();
            // пользователь вводит CRUD операцию, которую хочет выполнить, или выбирает выход из программы
            Button button = selectButton();
            commands.get(button).execute();
        }
    }

    @ExceptionHandling
    private static Button selectButton() {
        String userChoice = ScannerHolder.readStringValue();
        return Button.valueOf(userChoice.toUpperCase());
    }

    /**
     * Блок инициализации. Инстанцируем репозиторий и добавляем команды
     *
     * @return Хеш мапа команд
     */
    private static Map<Button, Command> initCommands() {
        UserRepository userRepository = new UserRepositoryImpl();
        Map<Button, Command> commands = new HashMap<>();
        commands.put(Button.EXIT, new ExitCommand());
        commands.put(Button.CREATE, new CreateUserCommand(userRepository));
        commands.put(Button.UPDATE, new UpdateUserCommand(userRepository));
        commands.put(Button.FIND, new FindUserCommand(userRepository));
        commands.put(Button.DELETE, new DeleteUserCommand(userRepository));
        commands.put(Button.ALL, new FindAllCommand(userRepository));
        commands.put(Button.INVALID_OPERATION, new InvalidOperationCommand());
        return commands;
    }

    /**
     * Подсказка для пользовательского ввода команд в интерактивном режиме
     */
    private static void printMenu() {
        String textInfo = """
                Enter the command:
                CREATE: create a new user
                UPDATE: update user info
                FIND: find user by id
                DELETE: delete user by id
                ALL: find all users
                EXIT: exit the program""";
        System.out.println(textInfo);
    }
}