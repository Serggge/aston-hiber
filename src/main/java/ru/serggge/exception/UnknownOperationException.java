package ru.serggge.exception;

public class UnknownOperationException extends RuntimeException {

    public UnknownOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
