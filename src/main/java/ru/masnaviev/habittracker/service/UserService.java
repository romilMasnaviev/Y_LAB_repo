package ru.masnaviev.habittracker.service;

import ru.masnaviev.habittracker.model.Role;
import ru.masnaviev.habittracker.model.User;
import ru.masnaviev.habittracker.out.repositories.InMemoryUserRepository;
import ru.masnaviev.habittracker.out.repositories.UserRepository;
import ru.masnaviev.habittracker.security.Session;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Класс UserService отвечает за логику управления пользователями,
 * включая создание, обновление, удаление и аутентификацию пользователей.
 */
public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new InMemoryUserRepository();
    }

    /**
     * Метод для создания нового пользователя.
     *
     * @param user объект пользователя, который необходимо создать
     * @return созданный пользователь
     * @throws IllegalArgumentException если пользователь с таким email уже существует
     */
    public User create(User user) {
        userAlreadyExists(user.getEmail());
        return userRepository.add(user);
    }

    /**
     * Метод для обновления данных пользователя.
     *
     * @param updatedUser объект с обновлёнными данными пользователя
     * @param id          идентификатор пользователя, которого необходимо обновить
     * @return обновлённый пользователь
     * @throws IllegalArgumentException если пользователь с таким email уже существует
     * @throws NoSuchElementException   если пользователь с указанным id не существует
     */
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

    /**
     * Метод для удаления пользователя по идентификатору.
     *
     * @param id идентификатор пользователя, которого необходимо удалить
     */
    public void delete(long id) {
        userRepository.delete(id);
    }

    /**
     * Метод для аутентификации пользователя.
     *
     * @param user объект пользователя с email и паролем для аутентификации
     * @return существующий пользователь, если аутентификация успешна
     * @throws IllegalArgumentException если пароль неверный
     * @throws NoSuchElementException   если пользователь с таким email не существует
     */
    public User authenticateUser(User user) {
        User existingUser = findByEmail(user.getEmail());

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Неправильный пароль для пользователя с email " + user.getEmail());
        }
        checkUserBlocked(existingUser);
        return existingUser;
    }

    /**
     * Метод для получения списка всех пользователей.
     *
     * @return список пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    /**
     * Метод для блокировки пользователя.
     *
     * @param userId идентификатор пользователя, которого необходимо заблокировать
     * @throws SecurityException      если попытка заблокировать администратора
     * @throws NoSuchElementException если пользователь с указанным id не существует
     */
    public void blockUser(long userId) {
        User user = get(userId);

        if (user.getRole().equals(Role.ADMIN)) {
            System.out.println("Нельзя заблокировать администратора");
        } else {
            user.setBlocked(true);
        }
    }

    /**
     * Метод для разблокировки пользователя.
     *
     * @param userId идентификатор пользователя, которого необходимо разблокировать
     */
    public void unblockUser(long userId) {
        userRepository.findById(userId).ifPresent(user -> user.setBlocked(false));
    }

    /**
     * Метод для проверки прав администратора.
     *
     * @param session объект сессии текущего пользователя
     * @throws SecurityException если у пользователя нет прав администратора
     */
    public void checkAdminRights(Session session) throws SecurityException {
        if (!session.getUser().getRole().equals(Role.ADMIN))
            throw new SecurityException("У вас нет прав администратора");
    }

    /**
     * Метод для проверки, заблокирован ли пользователь.
     *
     * @param user объект пользователя
     * @throws SecurityException если аккаунт пользователя заблокирован
     */
    public void checkUserBlocked(User user) {
        if (user.isBlocked())
            throw new SecurityException("Ваш аккаунт заблокирован");
    }

    /**
     * Метод для удаления пользователя администратором.
     *
     * @param userId идентификатор пользователя, которого необходимо удалить
     * @throws NoSuchElementException если пользователь с указанным id не существует
     */
    public void deleteUserByAdmin(long userId) {
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
