package ru.serggge;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import ru.serggge.command.Button;
import ru.serggge.entity.User;
import ru.serggge.util.CommandHolder;
import ru.serggge.util.ConsoleReader;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hibernate.cfg.JdbcSettings.*;
import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@Transactional
public class IntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");
    static EntityManagerFactory emf;
    @Mock
    static ConsoleReader consoleReader;
    static CommandHolder commandHolder;

    @BeforeAll
    static void init() throws InterruptedException {
        postgres
                .withInitScript("init.sql")
                .waitingFor(new LogMessageWaitStrategy()
                        .withRegEx(".*database system is ready to accept connections.*\\s")
                        .withTimes(2)
                        .withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS)))
                .start();
        Properties properties = testContainerProperties();
        consoleReader = Mockito.mock(ConsoleReader.class);
        commandHolder = new CommandHolder(properties, consoleReader);
        emf = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

    @AfterAll
    static void shutdown() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Create new user")
    void givenNewUser_whenExecuteOnCreateCommand_thenNewUserCreated() {
        // given
        final String username = "John";
        final String email = "john@email.org";
        final Integer age = ThreadLocalRandom.current()
                                             .nextInt(1, 100);
        // метод readStringValue() вызывается у мока 2 раза
        // через счётчик вызовов настраиваем ответ мока
        UnitTest.InvokeCounter invokeCounter = new UnitTest.InvokeCounter();
        when(consoleReader.readStringValue()).thenAnswer(invocationOnMock -> {
            if (invokeCounter.count++ == 0) {
                return username;
            } else {
                return email;
            }
        });
        when(consoleReader.readIntValue()).thenReturn(age);

        // when
        commandHolder.get(Button.CREATE)
                     .execute();

        // then
        List<User> allUsers = getAllUsers();
        assertThat(allUsers, hasSize(1));

        User user = allUsers.get(0);
        assertAll("Check User properties",
                () -> assertThat(user.getId(), notNullValue()),
                () -> assertThat(user.getName(), equalTo(username)),
                () -> assertThat(user.getEmail(), equalTo(email)),
                () -> assertThat(user.getAge(), equalTo(age)),
                () -> assertThat(user.getIsActive(), is(true)),
                () -> assertThat(user.getCreatedAt(), notNullValue()),
                () -> assertThat(user.getCreatedAt(), greaterThan(Instant.now()))
        );
    }

    List<User> getAllUsers() {
        try (EntityManager entityManager = emf.createEntityManager()) {
            return entityManager.createQuery("from User", User.class)
                                .getResultList();
        }
    }

    static Properties testContainerProperties() {
        Properties properties = new Properties();
        properties.put(JAKARTA_JDBC_DRIVER, postgres.getDriverClassName());
        properties.put(JAKARTA_JDBC_URL, postgres.getJdbcUrl());
        properties.put(JAKARTA_JDBC_USER, postgres.getUsername());
        properties.put(JAKARTA_JDBC_PASSWORD, postgres.getPassword());
        return properties;
    }
}
