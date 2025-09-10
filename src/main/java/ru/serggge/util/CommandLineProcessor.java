package ru.serggge.util;

import ru.serggge.config.Profile;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// парсер параметров запуска приложения
public class CommandLineProcessor {

    private static final Logger logger = Logger.getLogger(CommandLineProcessor.class.getName());

    private CommandLineProcessor() {
    }

    public static Profile readProfile(String[] args) {
        try {
            // извлекаем значения для параметра PROFILE
            String profilePattern = "--profile\\s(?<profile>[A-Za-z]+)";
            String parametersLine = String.join(" ", args);
            Matcher matcher = Pattern.compile(profilePattern)
                                     .matcher(parametersLine);
            if (matcher.find()) {
                String profileValue = matcher.group("profile");
                return Profile.valueOf(profileValue.toUpperCase());
            }
        } catch (RuntimeException e) {
            logger.warning("Can't process profile from command line");
        }
        logger.info("Profile is not define. Use default profile: DEVELOP");
        return Profile.DEVELOPMENT;
    }
}