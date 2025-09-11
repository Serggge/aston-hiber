package ru.serggge.util;

import ru.serggge.command.Button;
import ru.serggge.exception.UnknownOperationException;

public class ButtonSelector {

    private ButtonSelector() {
    }

    public static Button selectOperation() {
        // считываем в стандартном потоке ввода желаемую CRUD операцию, возвращаем соответствующий енум
        String userChoice = ConsoleReader.readStringValue();
        try {
            return Button.valueOf(userChoice.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownOperationException("Can't recognize the operation: " + userChoice, e);
        }
    }
}