package ru.serggge;

import ru.serggge.command.*;
import ru.serggge.util.ButtonHolder;
import ru.serggge.util.CommandSelector;

public class App {

    public static void main(String[] args) {
        CommandSelector commandSelector = new CommandSelector();
        // выполняем в бесконечном цикле, пока пользователь не выберет "Выход"
        while (true) {
            // печать в консоль информационного меню пользователя
            commandSelector.get(Button.PRINT_MENU)
                    .execute();
            // пользователь вводит CRUD операцию, которую хочет выполнить, или выбирает выход из программы
            Button operation = ButtonHolder.selectOperation();
            commandSelector.get(operation)
                    .execute();
        }
    }
}