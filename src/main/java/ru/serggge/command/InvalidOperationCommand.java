package ru.serggge.command;

public class InvalidOperationCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Input error. Unknown command. Try again!");
    }
}
