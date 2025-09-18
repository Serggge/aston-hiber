package ru.serggge;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.serggge.command.*;
import ru.serggge.dao.UserUserRepositoryImpl;
import ru.serggge.entity.User;
import ru.serggge.util.ConsoleReader;
import java.util.concurrent.ThreadLocalRandom;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandsTest {

    @InjectMocks
    CreateUserCommand createCommand;
    @InjectMocks
    UpdateUserCommand updateCommand;
    @InjectMocks
    FindUserCommand findCommand;
    @InjectMocks
    DeleteUserCommand deleteCommand;
    @Mock
    ConsoleReader consoleReader;
    @Mock
    UserUserRepositoryImpl repository;

    @Test
    @DisplayName("Create new user")
    void givenNewUser_whenExecuteOnCreateUserCommand_thenUserSaveInRepository() {
        // given
        final String username = "username";
        final String email = "user@eamil.org";
        final Integer age = ThreadLocalRandom.current().nextInt(1, 100);
        User user = new User(username, email, age);
        user.setId(1L);
        user.setIsActive(true);

        when(consoleReader.readStringValue()).thenReturn(username);
        when(consoleReader.readIntValue()).thenReturn(age);
        when(repository.save(any())).thenReturn(user);

        // when
        createCommand.execute();

        // then
        verify(consoleReader, atLeastOnce()).readStringValue();
        verify(consoleReader, times(1)).readIntValue();
        verifyNoMoreInteractions(consoleReader);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Find user by ID")
    void givenUserId_whenExecuteOnFindCommand_thenReturnUser() {
        long userId = ThreadLocalRandom.current().nextLong(1L, 100L);

        when(consoleReader.readLongValue()).thenReturn(userId);
        when(consoleReader.readBooleanValue()).thenReturn(true);

        // when
        findCommand.execute();

        // then
        verify(consoleReader, times(1)).readLongValue();
        verify(consoleReader, times(1)).readBooleanValue();
        verifyNoMoreInteractions(consoleReader);
        verify(repository).findById(userId);
    }

    @Test
    @DisplayName("Update user")
    void givenUpdatedUser_whenExecuteOnUpdateCommand_thenUserUpdatedInRepository() {
        // given
        final Long userId = ThreadLocalRandom.current().nextLong(1L, 100L);
        final String userName = "John";
        final Integer age = ThreadLocalRandom.current().nextInt(1, 100);
        when(consoleReader.readLongValue()).thenReturn(userId);
        when(consoleReader.readStringValue()).thenReturn(userName);
        when(consoleReader.readIntValue()).thenReturn(age);

        // when
        updateCommand.execute();

        // then
        verify(consoleReader, times(1)).readLongValue();
        verify(consoleReader, atLeastOnce()).readStringValue();
        verify(consoleReader, times(1)).readIntValue();
        verifyNoMoreInteractions(consoleReader);
        verify(repository).update(any());
    }

    @Test
    @DisplayName("Delete user")
    void givenUserId_whenExecuteOnDeleteUserCommand_thenDeleteUser() {
        // given
        long userId = ThreadLocalRandom.current().nextLong(1L, 100L);

        when(consoleReader.readLongValue()).thenReturn(userId);
        when(consoleReader.readBooleanValue()).thenReturn(true);

        // when
        deleteCommand.execute();

        // then
        verify(consoleReader, times(1)).readLongValue();
        verify(consoleReader, times(1)).readBooleanValue();
        verifyNoMoreInteractions(consoleReader);
        verify(repository, times(1)).deleteById(userId);
    }

}
