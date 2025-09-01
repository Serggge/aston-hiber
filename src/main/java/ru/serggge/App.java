package ru.serggge;

import ru.serggge.command.*;
import ru.serggge.dao.UserRepository;
import ru.serggge.dao.UserRepositoryImpl;
import java.util.*;

public class App {

    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        // инициализация команд
        Map<Button, Command> commands = initCommands();
        // выполняем в бесконечном цикле, пока пользователь не выберет "Выход"
        while (true) {
            printMenu();
            try {
                // пользователь вводит CRUD операцию или Выход, которую хочет выполнить, её и выполняем
                String userChoice = SCANNER.nextLine();
                Button button = Button.valueOf(userChoice.toUpperCase());
                commands.get(button).execute();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Input error. Try again:");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Блок инициализации. Инстанцируем репозиторий и добавляем команды
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
        return commands;
    }

    /**
     * Подсказка для пользовательского ввода команд в интерактивном режиме
     */
    private static void printMenu() {
        String textInfo = """
                Enter the command:создать нового пользователя
                
                CREATE: create a new user
                UPDATE: update user info
                FIND: find user by id
                DELETE: delete user by id
                EXIT: exit the program
                """;
        System.out.println(textInfo);
    }

    public enum Button {
        EXIT,
        CREATE,
        UPDATE,
        FIND,
        DELETE
    }
}