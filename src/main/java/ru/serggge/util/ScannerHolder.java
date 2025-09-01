package ru.serggge.util;

import ru.serggge.exception.UserInputException;
import java.util.Scanner;

public class ScannerHolder {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    private ScannerHolder() {

    }

    public static String readStringValue() {
        return scanner.next();
    }

    public static Long readLongValue() {
        String userInput = readStringValue();
        try {
            return Long.parseLong(userInput);
        } catch (Exception e) {
            throw new UserInputException("Can't parse to Long value: " + userInput, e);
        }
    }

    public static Integer readIntValue() {
        String userInput = readStringValue();
        try {
            return Integer.parseInt(userInput);
        } catch (Exception e) {
            throw new UserInputException("Can't parse to Integer value: " + userInput, e);
        }
    }
}