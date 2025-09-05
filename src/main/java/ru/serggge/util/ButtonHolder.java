package ru.serggge.util;

import ru.serggge.command.Button;
import ru.serggge.interceptors.ExceptionHandling;

public class ButtonHolder {

    private ButtonHolder() {
    }

    @ExceptionHandling
    public static Button selectOperation() {
        // считываем в стандартном потоке ввода операцию, возвращаем соответствующий енум
        String userChoice = OperationReader.readStringValue();
        return Button.valueOf(userChoice.toUpperCase());
    }
}