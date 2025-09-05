package ru.serggge;

import ru.serggge.command.*;
import ru.serggge.util.ButtonHolder;
import ru.serggge.util.CommandSelector;
import ru.serggge.util.ExceptionHandler;

public class App {

    public static void main(String[] args) {
        // предоставляет набор объектов типа "команда", обрабатывающих CRUD операции
        CommandSelector commandSelector = new CommandSelector();
        // выполняем в бесконечном цикле, пока пользователь не выберет "Выход"
        while (true) {
            // печать в консоль информационного меню пользователя
            commandSelector.get(Button.PRINT_MENU)
                           .execute();
            try {
                // здесь пользователь вводит CRUD операцию, которую хочет выполнить
                Button operation = ButtonHolder.selectOperation();
                // здесь выполняем её через соответствующий объект команды
                commandSelector.get(operation)
                               .execute();
            } catch (RuntimeException e) {
                ExceptionHandler.handle(e);
            }
        }
    }
}