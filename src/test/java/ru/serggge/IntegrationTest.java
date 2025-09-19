package ru.serggge;


import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.serggge.command.CreateUserCommand;
import ru.serggge.config.DataSourceConfig;
import ru.serggge.config.Profile;
import ru.serggge.util.CommandHolder;
import ru.serggge.util.ConsoleReader;

import java.util.Properties;

import static org.hibernate.cfg.JdbcSettings.*;
import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_PASSWORD;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

public class IntegrationTest {

    static PostgreSQLContainer<?> postgres;
    static CommandHolder commandHolder;
    static ConsoleReader consoleReader;

    @BeforeAll
    static void init() throws InterruptedException {
        DataSourceConfig dataSourceConfig = new DataSourceConfig(Profile.TEST);
        Properties properties = dataSourceConfig.getProperties();
        try (var container = new PostgreSQLContainer<>("postgres")) {
            container.withDatabaseName(properties.getProperty())
            postgres = container.withExposedPorts(5555);
        }
        postgres.start();
        while (!postgres.isHealthy()) {
            Thread.currentThread().wait(1000);
        }
        consoleReader = Mockito.mock(ConsoleReader.class);
        commandHolder = new CommandHolder(Profile.TEST, consoleReader);
    }

    @AfterAll
    static void shutdown() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {

        String jdbcUrl = postgres.getJdbcUrl();
        Integer jdbcPort = postgres.getMappedPort(5555);
        String databaseName = postgres.getDatabaseName();
        String driverClassName = postgres.getDriverClassName();
        String username = postgres.getUsername();
        String password = postgres.getPassword();

        Properties properties = new Properties();
        properties.put(JAKARTA_JDBC_DRIVER, driverClassName);
        properties.put(JAKARTA_JDBC_URL, String.format("jdbc:postgresql://%s:%d/%s", jdbcUrl, jdbcPort, databaseName));
        properties.put(JAKARTA_JDBC_USER, username);
        properties.put(JAKARTA_JDBC_PASSWORD, password);
        properties.put(HBM2DDL_AUTO, "create-drop");
    }

}
