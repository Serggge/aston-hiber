package ru.serggge.util;

import lombok.extern.slf4j.Slf4j;
import ru.serggge.config.Profile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// парсер параметров запуска приложения
@Slf4j
public class CommandLineProcessor {

    private CommandLineProcessor() {
    }

    public static Profile readProfile(String[] args) {
        try {
            // извлекаем значения для параметра PROFILE
            String profilePattern = "-p\\s(?<profile>[A-Za-z]+)";
            String parametersLine = String.join(" ", args);
            Matcher matcher = Pattern.compile(profilePattern)
                                     .matcher(parametersLine);
            if (matcher.find()) {
                String profileValue = matcher.group("profile");
                return Profile.valueOf(profileValue.toUpperCase());
            }
        } catch (RuntimeException e) {
            log.warn("Can't process profile from command line");
        }
        log.info("Profile is not define. Use default profile: DEVELOP");
        return Profile.DEVELOPMENT;
    }
}