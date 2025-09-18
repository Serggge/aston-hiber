package ru.serggge;

import lombok.extern.slf4j.Slf4j;
import ru.serggge.command.*;
import ru.serggge.config.Profile;
import ru.serggge.util.*;

@Slf4j
public class App {

    public static void main(String[] args) {
        // определяем профиль, с которым выполняется приложение (DEVELOP/PRODUCTION)
        Profile profile = CommandLineProcessor.readProfile(args);
        // создаём объект, который будет управлять командами, обрабатывающими CRUD операции
        CommandHolder commandHolder = new CommandHolder(profile);
        // выполняем в бесконечном цикле, пока пользователь не выберет "Выход"
        while (true) {
            // печать в консоль информационного меню пользователя
            commandHolder.get(Button.PRINT_MENU).execute();
            try {
                // здесь пользователь вводит CRUD операцию, которую хочет выполнить
                ConsoleReader consoleReader = new ConsoleReader();
                Button operation = consoleReader.readOperation();
                // здесь выполняем её через соответствующий объект команды
                commandHolder.get(operation).execute();
            } catch (RuntimeException e) {
                // обрабатываем выброшенные исключения
                log.warn("Error occurred: {}", e.getMessage());
            }
        }
    }
}