package ru.serggge.util;

import org.hibernate.HibernateException;
import ru.serggge.exception.UserInputException;
import java.sql.SQLException;

public class ExceptionHandler {

    private ExceptionHandler() {
    }

    // блок централизованной обработки Exception
    public static void handle(Exception e) {
        if (e.getClass().isAssignableFrom(UserInputException.class)) {
            System.out.println(e.getMessage());
        } else if (e instanceof SQLException) {
            System.out.println("An error occurred while accessing the database:" + e.getMessage());
        } else if (e instanceof HibernateException) {
            System.out.println("An error occurred in Hibernate: " + e.getMessage());
        } else {
            System.out.println("An unexpected error occurred:" + e.getMessage());
        }
    }
}
