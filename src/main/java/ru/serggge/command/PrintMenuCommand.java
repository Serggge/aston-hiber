package ru.serggge.command;

public class PrintMenuCommand implements Command {

    @Override
    public void execute() {
        String textInfo = """
                Enter the command:
                CREATE: create a new user
                UPDATE: update user info
                FIND: find user by id
                DELETE: delete user by id
                ALL: find all users
                EXIT: exit the program""";
        System.out.println(textInfo);
    }
}
