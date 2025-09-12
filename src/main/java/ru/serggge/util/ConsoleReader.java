package ru.serggge.util;

import ru.serggge.command.Button;
import ru.serggge.exception.UnknownOperationException;
import ru.serggge.exception.UserInputException;
import java.util.Scanner;

// Утилитный класс, отвечающий за обработку пользовательского ввода операций в консоли
public class ConsoleReader {

    private static final String ANSWER_YES = "Y";
    private static final String ANSWER_NO = "N";
    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleReader() {
    }

    public static String readStringValue() {
        return scanner.next();
    }

    public static Long readLongValue() {
        String userInput = readStringValue();
        try {
            return Long.parseLong(userInput);
        } catch (Exception e) {
            throw new UserInputException("Can't parse to Long value: " + userInput, e);
        }
    }

    public static Integer readIntValue() {
        String userInput = readStringValue();
        try {
            return Integer.parseInt(userInput);
        } catch (Exception e) {
            throw new UserInputException("Can't parse to Integer value: " + userInput, e);
        }
    }

    public static boolean readBooleanValue() {
        String userInput = scanner.next();
        return switch (userInput.toUpperCase()) {
            case "Y" -> true;
            case "N" -> false;
            default -> throw new UserInputException(String.format(
                    "Syntax error. Can't recognize the answer '%s'. Acceptable values: {'%s' / '%s'}\n",
                            userInput, ANSWER_YES, ANSWER_NO));
        };
    }

    public static Button readOperation() {
        // считываем в стандартном потоке ввода желаемую CRUD операцию, возвращаем соответствующий енум
        String userInput = readStringValue();
        try {
            return Button.valueOf(userInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownOperationException("Syntax error. Unknown operation: " + userInput, e);
        }
    }
}