package ru.serggge.exception;

public class UserInputException extends RuntimeException {

    public UserInputException(String message, Throwable cause) {
        super(message, cause);
    }
}