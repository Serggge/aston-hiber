package ru.serggge;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.serggge.command.*;
import ru.serggge.dao.UserUserRepositoryImpl;
import ru.serggge.entity.User;
import ru.serggge.util.ConsoleReader;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandsTest {

    @Mock
    UserUserRepositoryImpl repository;
    @Mock
    ConsoleReader consoleReader;
    @InjectMocks
    CreateUserCommand createCommand;
    @InjectMocks
    UpdateUserCommand updateCommand;
    @InjectMocks
    FindUserCommand findCommand;
    @InjectMocks
    DeleteUserCommand deleteCommand;
    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Create new user")
    void givenNewUser_whenExecuteOnCreateUserCommand_thenUserSavedInRepository() {
        // given
        final String username = "username";
        final String email = "user@eamil.org";
        final Integer age = ThreadLocalRandom.current().nextInt(1, 100);
        User user = new User(username, email, age);
        // метод readStringValue() вызывается у мока 2 раза
        // через счётчик вызовов настраиваем ответ мока
        InvokeCounter invokeCounter = new InvokeCounter();
        when(consoleReader.readStringValue()).thenAnswer(invocationOnMock -> {
            if (invokeCounter.count++ == 0) {
                return username;
            } else {
                return email;
            }
        });
        when(consoleReader.readIntValue()).thenReturn(age);
        when(repository.save(userCaptor.capture())).thenReturn(user);

        // when
        createCommand.execute();

        // then
        verify(consoleReader, times(2)).readStringValue();
        verify(consoleReader, times(1)).readIntValue();
        verifyNoMoreInteractions(consoleReader);
        verify(repository).save(userCaptor.getValue());

        assertThat(user, equalTo(userCaptor.getValue()));
    }

    @Test
    @DisplayName("Find user by ID")
    void givenUserId_whenExecuteOnFindCommand_thenReturnUser() {
        // given
        long userId = ThreadLocalRandom.current().nextLong(1L, 100L);
        final User user = new User("John", "john@email.org", 30);
        user.setId(userId);

        when(consoleReader.readLongValue()).thenReturn(userId);
        when(consoleReader.readBooleanValue()).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

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
        Long userId = ThreadLocalRandom.current().nextLong(1L, 100L);
        String userName = "John";
        String email = "john@email.org";
        Integer age = ThreadLocalRandom.current().nextInt(1, 100);
        User user = new User(userName, email, age);
        user.setId(userId);

        when(consoleReader.readLongValue()).thenReturn(userId);
        // используем счётчик вызовов метода readStringValue() для настройки ответа мока
        InvokeCounter invokeCounter = new InvokeCounter();
        when(consoleReader.readStringValue()).thenAnswer(invocationOnMock -> {
           if (invokeCounter.count++ == 0) {
               return userName;
           } else {
               return email;
           }
        });
        when(consoleReader.readIntValue()).thenReturn(age);
        when(repository.update(userCaptor.capture())).thenReturn(user);

        // when
        updateCommand.execute();

        // then
        verify(consoleReader, times(1)).readLongValue();
        verify(consoleReader, atLeastOnce()).readStringValue();
        verify(consoleReader, times(1)).readIntValue();
        verifyNoMoreInteractions(consoleReader);
        verify(repository).update(userCaptor.getValue());

        assertThat(user, equalTo(userCaptor.getValue()));
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

    static class InvokeCounter {
        int count;
    }

}
