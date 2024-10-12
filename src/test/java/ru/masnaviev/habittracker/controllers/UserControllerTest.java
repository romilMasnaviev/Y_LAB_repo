package ru.masnaviev.habittracker.controllers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.masnaviev.habittracker.in.converter.UserConverter;
import ru.masnaviev.habittracker.in.dto.CreateUserRequest;
import ru.masnaviev.habittracker.in.dto.LoginUserRequest;
import ru.masnaviev.habittracker.in.dto.UpdateUserRequest;
import ru.masnaviev.habittracker.models.User;
import ru.masnaviev.habittracker.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    UserService userService;
    @Mock
    UserConverter userConverter;
    @InjectMocks
    UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        CreateUserRequest request = new CreateUserRequest("test@example.com", "password", "name");
        User user = new User(request.getEmail(), request.getPassword(), request.getName());

        when(userConverter.createUserRequestConvertToUser(request)).thenReturn(user);
        when(userService.create(any())).thenReturn(user);

        User createdUser = userController.create(request);

        assertEquals(user, createdUser);
    }


    @Test
    void testCreateUser_InvalidEmail() {
        CreateUserRequest request = new CreateUserRequest("", "password", "name");

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> userController.create(request));

        assertEquals("Почта не может быть пустой.", thrown.getMessage());
        verify(userService, never()).create(any(User.class));
    }

    @Test
    void testCreateUser_InvalidName() {
        CreateUserRequest request = new CreateUserRequest("test@example.com", "password", "");

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> userController.create(request));

        assertEquals("Имя не может быть пустым.", thrown.getMessage());
        verify(userService, never()).create(any(User.class));
    }

    @Test
    void testCreateUser_InvalidPassword() {
        CreateUserRequest request = new CreateUserRequest("test@example.com", "", "name");

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> userController.create(request));

        assertEquals("Пароль не может быть пустой.", thrown.getMessage());
        verify(userService, never()).create(any(User.class));
    }

    @Test
    void testUpdate_Success() {
        long userId = 1L;
        User updatedUser = new User("test@example.com", "new_password", "new_name");
        UpdateUserRequest request = new UpdateUserRequest(updatedUser.getEmail(), updatedUser.getPassword(), updatedUser.getName());

        when(userConverter.updateUserRequestConvertToUser(request)).thenReturn(updatedUser);
        when(userService.update(any(User.class), any(Long.class))).thenReturn(updatedUser);
        User result = userController.update(request, userId);

        assertEquals(updatedUser, result);
    }

    @Test
    void testUpdateUser_InvalidData() {
        long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("123", "new_password", "new_name");

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> userController.update(request, userId));

        assertEquals("Почта должна содержать '@'.", thrown.getMessage());
        verify(userService, never()).update(any(User.class), eq(userId));
    }

    @Test
    void testDelete_Success() {
        long userId = 1L;

        userController.delete(userId);

        verify(userService, times(1)).delete(userId);
    }

    @Test
    void testAuthenticateUser_Success() {
        LoginUserRequest request = new LoginUserRequest("test@example.com", "password");
        User user = new User(request.getEmail(), request.getPassword(), "name");

        when(userConverter.loginUserRequestConvertToUser(any(LoginUserRequest.class))).thenReturn(user);
        when(userService.authenticateUser(any(User.class))).thenReturn(user);

        User authenticatedUser = userController.authenticateUser(request);

        assertEquals(user, authenticatedUser);
        verify(userService, times(1)).authenticateUser(any());
    }

}