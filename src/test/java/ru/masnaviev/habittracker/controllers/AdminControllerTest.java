package ru.masnaviev.habittracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.masnaviev.habittracker.models.Habit;
import ru.masnaviev.habittracker.models.User;
import ru.masnaviev.habittracker.security.Session;
import ru.masnaviev.habittracker.services.HabitService;
import ru.masnaviev.habittracker.services.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HabitService habitService;


    @InjectMocks
    private AdminController adminController;

    private Session adminSession;
    private Session regularUserSession;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminSession = mock(Session.class);
        when(adminSession.isAdmin()).thenReturn(true);

        regularUserSession = mock(Session.class);
        when(regularUserSession.isAdmin()).thenReturn(false);
    }

    @Test
    void testGetAllUsers_Success() {
        User user1 = new User("user1@example.com", "password1", "User One");
        User user2 = new User("user2@example.com", "password2", "User Two");
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = adminController.getAllUsers(adminSession);

        assertEquals(users, result);
        verify(userService).checkAdminRights(adminSession);
    }

    @Test
    void testGetAllUsers_NoAdminRights() {

        doThrow(new SecurityException("У вас нет прав администратора")).when(userService).checkAdminRights(regularUserSession);

        SecurityException thrown = assertThrows(SecurityException.class, () -> adminController.getAllUsers(regularUserSession));

        assertEquals("У вас нет прав администратора", thrown.getMessage());
        verify(userService).checkAdminRights(regularUserSession);
    }

    @Test
    void testBlockUser_Success() {
        long userId = 1L;

        adminController.blockUser(userId, adminSession);

        verify(userService).checkAdminRights(adminSession);
        verify(userService).blockUser(userId);
    }

    @Test
    void testBlockUser_NoAdminRights() {
        long userId = 1L;
        doThrow(new SecurityException("У вас нет прав администратора")).when(userService).checkAdminRights(regularUserSession);

        SecurityException thrown = assertThrows(SecurityException.class, () -> adminController.blockUser(userId, regularUserSession));

        assertEquals("У вас нет прав администратора", thrown.getMessage());
        verify(userService).checkAdminRights(regularUserSession);
    }

    @Test
    void testUnblockUser_Success() {
        long userId = 1L;

        adminController.unblockUser(userId, adminSession);

        verify(userService).checkAdminRights(adminSession);
        verify(userService).unblockUser(userId);
    }

    @Test
    void testUnblockUser_NoAdminRights() {
        long userId = 1L;
        doThrow(new SecurityException("У вас нет прав администратора")).when(userService).checkAdminRights(regularUserSession);

        SecurityException thrown = assertThrows(SecurityException.class, () -> adminController.unblockUser(userId, regularUserSession));

        assertEquals("У вас нет прав администратора", thrown.getMessage());
        verify(userService).checkAdminRights(regularUserSession);
    }

    @Test
    void testDeleteUser_Success() {
        long userId = 1L;

        adminController.deleteUser(userId, adminSession);

        verify(userService).checkAdminRights(adminSession);
        verify(userService).deleteUserByAdmin(userId);
    }

    @Test
    void testDeleteUser_NoAdminRights() {
        long userId = 1L;
        doThrow(new SecurityException("У вас нет прав администратора")).when(userService).checkAdminRights(regularUserSession);

        SecurityException thrown = assertThrows(SecurityException.class, () -> adminController.deleteUser(userId, regularUserSession));

        assertEquals("У вас нет прав администратора", thrown.getMessage());
        verify(userService).checkAdminRights(regularUserSession);
    }

    @Test
    void testGetAllHabits_Success() {
        List<Habit> habits = List.of(new Habit("Habit 1", "Description 1", null),
                new Habit("Habit 2", "Description 2", null));
        when(habitService.getAllHabits()).thenReturn(habits);

        adminController.getAllHabits(adminSession);

        verify(userService).checkAdminRights(adminSession);
        verify(habitService).getAllHabits();
    }

    @Test
    void testGetAllHabits_NoAdminRights() {
        doThrow(new SecurityException("У вас нет прав администратора")).when(userService).checkAdminRights(regularUserSession);

        SecurityException thrown = assertThrows(SecurityException.class, () -> adminController.getAllHabits(regularUserSession));

        assertEquals("У вас нет прав администратора", thrown.getMessage());
        verify(userService).checkAdminRights(regularUserSession);
    }

    @Test
    void testDeleteHabit_Success() {
        long habitId = 1L;

        adminController.deleteHabit(habitId, adminSession);

        verify(userService).checkAdminRights(adminSession);
        verify(habitService).deleteHabit(habitId);
    }

    @Test
    void testDeleteHabit_NoAdminRights() {
        long habitId = 1L;
        doThrow(new SecurityException("У вас нет прав администратора")).when(userService).checkAdminRights(regularUserSession);

        SecurityException thrown = assertThrows(SecurityException.class, () -> adminController.deleteHabit(habitId, regularUserSession));

        assertEquals("У вас нет прав администратора", thrown.getMessage());
        verify(userService).checkAdminRights(regularUserSession);
    }

}