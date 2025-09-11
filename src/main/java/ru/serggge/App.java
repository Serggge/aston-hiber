package ru.serggge;

import ru.serggge.command.*;
import ru.serggge.config.Profile;
import ru.serggge.util.ButtonSelector;
import ru.serggge.util.CommandLineProcessor;
import ru.serggge.util.CommandHolder;
import ru.serggge.util.ExceptionHandler;

public class App {

    //  переменная, позволяющий менять настройки конфигурации в зависимости от среды выполнения
    public static Profile profile;

    public static void main(String[] args) {
        // определяем профиль, с которым выполняется приложение (DEVELOP/PRODUCTION)
        profile = CommandLineProcessor.readProfile(args);
        // создаём объект, который будет управлять командами, обрабатывающими CRUD операции
        CommandHolder commandHolder = new CommandHolder();
        // выполняем в бесконечном цикле, пока пользователь не выберет "Выход"
        while (true) {
            // печать в консоль информационного меню пользователя
            commandHolder.get(Button.PRINT_MENU).execute();
            try {
                // здесь пользователь вводит CRUD операцию, которую хочет выполнить
                Button operation = ButtonSelector.selectOperation();
                // здесь выполняем её через соответствующий объект команды
                commandHolder.get(operation).execute();
            } catch (RuntimeException e) {
                // обрабатываем выброшенные исключения
                ExceptionHandler.handle(e);
            }
        }
    }
}