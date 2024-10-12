package ru.masnaviev.habittracker.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.masnaviev.habittracker.models.Role;
import ru.masnaviev.habittracker.models.User;
import ru.masnaviev.habittracker.out.repositories.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void checkUserBlocked_shouldThrowSecurityException_whenUserIsBlocked() {
        User user = new User(Role.ADMIN, 1L, "test@example.com", "paswword", "name", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(SecurityException.class, () -> userService.checkUserBlocked(user));
    }

    @Test
    void checkUserBlocked_shouldNotThrowException_whenUserIsNotBlocked() {
        User user = new User(Role.USER, 1L, "test@example.com", "paswword", "name", false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.checkUserBlocked(user));
    }

    @Test
    void deleteUserByAdmin_shouldDeleteUser_whenUserIsNotAdmin() {
        User user = new User(Role.USER, 1L, "test@example.com", "paswword", "name", false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserByAdmin(1L);

        verify(userRepository, times(1)).delete(1L);
    }

    @Test
    void deleteUserByAdmin_shouldNotDeleteUser_whenUserIsAdmin() {
        User user = new User(Role.ADMIN, 1L, "test@example.com", "paswword", "name", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserByAdmin(1L);

        verify(userRepository, never()).delete(1L);
    }

    @Test
    void update_shouldUpdateExistingUser_whenEmailNotChanged() {
        long userId = 1L;
        User existingUser = new User(Role.USER, userId, "test@example.com", "password", "test", false);
        User updatedUser = new User(Role.USER, userId, null, "password", "updatedName", false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User result = userService.update(updatedUser, userId);

        assertEquals("updatedName", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void update_shouldUpdateExistingUser_whenEmailChangedToUnusedEmail() {
        long userId = 1L;
        User existingUser = new User(Role.USER, userId, "test@example.com", "password", "test", false);
        User updatedUser = new User(Role.USER, userId, "updated@example.com", "password", "updatedName", false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());

        User result = userService.update(updatedUser, userId);

        assertEquals("updated@example.com", result.getEmail());
        assertEquals("updatedName", result.getName());
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenEmailChangedToExistingEmail() {
        long userId = 1L;
        long anotherUserId = 2L;
        User existingUser = new User(Role.USER, userId, "test@example.com", "password", "test", false);
        User anotherUser = new User(Role.USER, anotherUserId, "updated@example.com", "password", "test", false);
        User updatedUser = new User(Role.USER, userId, "updated@example.com", "password", "updatedName", false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.of(anotherUser));

        assertThrows(IllegalArgumentException.class, () -> userService.update(updatedUser, userId));
    }

    @Test
    void update_shouldThrowNoSuchElementException_whenUserDoesNotExist() {
        long userId = 1L;
        User updatedUser = new User(Role.USER, userId, "updated@example.com", "password", "updatedName", false);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.update(updatedUser, userId));
    }

    @Test
    void authenticateUser_shouldReturnExistingUser_whenCredentialsAreCorrect() {
        long userId = 1L;
        String email = "test@example.com";
        String password = "password";
        User existingUser = new User(Role.USER, userId, email, password, "test", false);
        User userToAuthenticate = new User(Role.USER, userId, email, password, "test", false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        User authenticatedUser = userService.authenticateUser(userToAuthenticate);

        assertEquals(existingUser, authenticatedUser);
    }

    @Test
    void authenticateUser_shouldThrowIllegalArgumentException_whenPasswordIsIncorrect() {
        long userId = 1L;
        String email = "test@example.com";
        String password = "password";
        String incorrectPassword = "wrongPassword";
        User existingUser = new User(Role.USER, userId, email, password, "test", false);
        User userToAuthenticate = new User(Role.USER, userId, email, incorrectPassword, "test", false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> userService.authenticateUser(userToAuthenticate));
    }

    @Test
    void blockUser_shouldSetBlockedToTrue_whenUserIsNotAdmin() {
        long userId = 1L;
        User user = new User(Role.USER, userId, "test@example.com", "password", "test", false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.blockUser(userId);

        assertTrue(user.isBlocked());
    }

    @Test
    void blockUser_shouldNotSetBlockedToTrue_whenUserIsAdmin() {
        long userId = 1L;
        User user = new User(Role.ADMIN, userId, "test@example.com", "password", "test", false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.blockUser(userId);

        assertFalse(user.isBlocked());
    }
}
