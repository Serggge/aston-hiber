package ru.serggge.util;

import ru.serggge.command.Button;
import ru.serggge.interceptors.ExceptionHandling;

public class ButtonHolder {

    private ButtonHolder() {
    }

    @ExceptionHandling
    public static Button selectOperation() {
        String userChoice = ScannerHolder.readStringValue();
        return Button.valueOf(userChoice.toUpperCase());
    }
}