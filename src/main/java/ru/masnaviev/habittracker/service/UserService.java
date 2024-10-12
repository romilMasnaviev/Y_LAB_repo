package ru.masnaviev.habittracker.service;

import ru.masnaviev.habittracker.model.Role;
import ru.masnaviev.habittracker.model.Session;
import ru.masnaviev.habittracker.model.User;
import ru.masnaviev.habittracker.repositories.InMemoryUserRepository;
import ru.masnaviev.habittracker.repositories.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new InMemoryUserRepository();
    }

    public User create(User user) {
        userAlreadyExists(user.getEmail());
        return userRepository.add(user);
    }

    public User update(User updatedUser, long id) {
        User existingUser = get(id);
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            Optional<User> userWithEmail = userRepository.findByEmail(updatedUser.getEmail());
            if (userWithEmail.isPresent() && userWithEmail.get().getId() != id) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует");
            }
        }
        copyNonNullFields(updatedUser, existingUser);
        return existingUser;
    }

    public void delete(long id) {
        userRepository.delete(id);
    }

    public User authenticateUser(User user) {
        User existingUser = findByEmail(user.getEmail());

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Неправильный пароль для пользователя с email " + user.getEmail());
        }
        checkUserBlocked(existingUser);
        return existingUser;
    }


    private void userAlreadyExists(String email) {
        if (userRepository.isExistsByEmail(email))
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Пользователя с таким email не существует"));
    }

    private User get(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователя с таким id не существует"));
    }

    private void copyNonNullFields(User source, User target) {
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }
        if (source.getPassword() != null) {
            target.setPassword(source.getPassword());
        }
    }

    public List<User> getAllUsers(Session session) {
        checkAdminRights(session);
        return userRepository.getAll();
    }

    public void blockUser(long userId, Session session) {
        checkAdminRights(session);
        User user = get(userId);

        if (user.getRole().equals(Role.ADMIN)) {
            System.out.println("Нельзя заблокировать администратора");
        } else {
            user.setBlocked(true);
        }
    }

    public void unblockUser(long userId, Session session) {
        checkAdminRights(session);
        userRepository.findById(userId).ifPresent(user -> user.setBlocked(false));
    }

    public void checkAdminRights(Session session) {
        if (!session.getUser().getRole().equals(Role.ADMIN))
            throw new SecurityException("У вас нет прав администратора");
    }

    public void checkUserBlocked(User user) {
        if (user.isBlocked())
            throw new SecurityException("Ваш аккаунт заблокирован");
    }

    public void deleteUserByAdmin(long userId, Session session) {
        checkAdminRights(session);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getRole().equals(Role.ADMIN)) {
                System.out.println("Нельзя заблокировать администратора");
            } else {
                userRepository.delete(userId);
            }
        }
    }
}
