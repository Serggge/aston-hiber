package ru.serggge.util;

import ru.serggge.command.Button;

public class ButtonHolder {

    private ButtonHolder() {
    }

    public static Button selectOperation() {
        // считываем в стандартном потоке ввода операцию, возвращаем соответствующий енум
        String userChoice = OperationReader.readStringValue();
        try {
            return Button.valueOf(userChoice.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Button.INVALID_OPERATION;
        }
    }
}