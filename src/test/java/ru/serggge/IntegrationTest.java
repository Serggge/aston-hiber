package ru.serggge;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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
        try (EntityManager entityManager = emf.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.createQuery("DELETE FROM User")
                    .executeUpdate();
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Create new user")
    void givenNewUser_whenExecuteOnCreateCommand_thenNewUserPersistToDatabase() {
        // given
        final String username = "John";
        final String email = "john@email.org";
        final Integer age = ThreadLocalRandom.current().nextInt(1, 100);
        // метод readStringValue() вызывается у мока 2 раза
        // через счётчик вызовов настраиваем ответ мока
        InvokeCounter invokeCounter = new InvokeCounter();
        when(consoleReader.readStringValue()).thenAnswer(invocationOnMock ->
                invokeCounter.count++ == 0 ? username : email);
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


    @Test
    @DisplayName("Find user")
    void givenUserId_whenExecuteOnFindCommand_thenReturnUser() {
        //given
        final User user = generateUser();
        saveToDatabase(user);

        when(consoleReader.readLongValue()).thenReturn(user.getId());
        when(consoleReader.readBooleanValue()).thenReturn(true);

        // when
        commandHolder.get(Button.FIND)
                     .execute();

        // then
        List<User> allUsers = getAllUsers();
        assertThat(allUsers, hasSize(1));
    }

    @Test
    @DisplayName("Update user")
    void givenUserWithUpdatedFields_whenExecuteOnUpdateCommand_thenUpdateUserIntoDatabase() {
        //given
        User originalUser = generateUser();
        saveToDatabase(originalUser);

        // заготовки ответов для мока ConsoleReader
        final long userId = originalUser.getId();
        final String newName = "Katty";
        final String newEmail = "katty@email.org";
        final Integer newAge = ThreadLocalRandom.current().nextInt(1, 100);

        when(consoleReader.readLongValue()).thenReturn(userId);
        // метод readStringValue() вызывается у мока 2 раза
        // через счётчик вызовов настраиваем ответ мока
        InvokeCounter invokeCounter = new InvokeCounter();
        when(consoleReader.readStringValue()).thenAnswer(invocationOnMock ->
                invokeCounter.count++ == 0 ? newName : newEmail);
        when(consoleReader.readIntValue()).thenReturn(newAge);

        // when
        commandHolder.get(Button.UPDATE)
                     .execute();

        // then
        User persistenceUser = findById(userId);

        assertAll("Check user properties",
                () -> assertThat(persistenceUser.getName(), equalTo(newName)),
                () -> assertThat(persistenceUser.getEmail(), equalTo(newEmail)),
                () -> assertThat(persistenceUser.getAge(), is(newAge))
        );
    }

    @Test
    @DisplayName("Delete user")
    void givenUserId_whenExecuteOnDeleteCommand_thenDeleteUserFromDatabase() {
        //given
        User user = generateUser();
        saveToDatabase(user);
        long userId = user.getId();

        when(consoleReader.readLongValue()).thenReturn(userId);
        when(consoleReader.readBooleanValue()).thenReturn(true);

        // when
        commandHolder.get(Button.DELETE)
                     .execute();

        // then
        List<User> allUsers = getAllUsers();
        User checkedUser = findById(userId);

        assertThat(allUsers, empty());
        assertThat(checkedUser, nullValue());
    }

    @Test
    @DisplayName("Deactivate user")
    void givenUserId_whenExecuteOnDeleteCommand_thenSetUserActivityFalse() {
        //given
        User user = generateUser();
        saveToDatabase(user);
        long userId = user.getId();

        when(consoleReader.readLongValue()).thenReturn(userId);
        when(consoleReader.readBooleanValue()).thenReturn(false);

        // when
        commandHolder.get(Button.DELETE)
                     .execute();

        // then
        List<User> allUsers = getAllUsers();
        User checkedUser = findById(userId);

        assertThat(allUsers, hasSize(1));
        assertThat(checkedUser.getIsActive(), is(false));
    }

    User findById(Long userId) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            return entityManager.find(User.class, userId);
        }
    }

    void saveToDatabase(User user) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
        }
    }

    List<User> getAllUsers() {
        try (EntityManager entityManager = emf.createEntityManager()) {
            return entityManager.createQuery("from User", User.class)
                                .getResultList();
        }
    }

    private User generateUser() {
        final String username = "User" + ThreadLocalRandom.current().nextInt();
        final String email = username + "@email.org";
        final int age = ThreadLocalRandom.current().nextInt(1, 100);
        return new User(username, email, age);
    }

    static Properties testContainerProperties() {
        Properties properties = new Properties();
        properties.put(JAKARTA_JDBC_DRIVER, postgres.getDriverClassName());
        properties.put(JAKARTA_JDBC_URL, postgres.getJdbcUrl());
        properties.put(JAKARTA_JDBC_USER, postgres.getUsername());
        properties.put(JAKARTA_JDBC_PASSWORD, postgres.getPassword());
        return properties;
    }

    static class InvokeCounter {
        int count;
    }
}
