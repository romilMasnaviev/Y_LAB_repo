package ru.masnaviev.habittracker.service;

import ru.masnaviev.habittracker.model.User;
import ru.masnaviev.habittracker.repositories.InMemoryUserRepository;
import ru.masnaviev.habittracker.repositories.UserRepository;

import java.util.NoSuchElementException;

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
        return user;
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
}
