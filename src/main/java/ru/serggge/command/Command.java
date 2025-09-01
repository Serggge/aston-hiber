package ru.serggge.command;

import ru.serggge.App;

public interface Command {

    void execute();

    // метод для считывания значений типа Long из консоли ввода пользователя
    default long nextLong() {
        try {
            return App.SCANNER.nextLong();
        } catch (Exception e) {
            System.out.println("Input error");
            return nextLong();
        }
    }

    // метод для считывания значений типа Integer из консоли ввода пользователя
    default int nextInt() {
        try {
            return App.SCANNER.nextInt();
        } catch (Exception e) {
            System.out.println("Input error");
            return nextInt();
        }
    }

    // метод для считывания значений типа String из консоли ввода пользователя
    default String nextLine() {
        try {
            return App.SCANNER.next();
        } catch (Exception e) {
            System.out.println("Input error");
            return nextLine();
        }
    }
}
