package ru.masnaviev.habittracker.controllers;

import ru.masnaviev.habittracker.in.converter.UserConverter;
import ru.masnaviev.habittracker.in.dto.CreateUserRequest;
import ru.masnaviev.habittracker.in.dto.LoginUserRequest;
import ru.masnaviev.habittracker.in.dto.UpdateUserRequest;
import ru.masnaviev.habittracker.model.User;
import ru.masnaviev.habittracker.service.UserService;

/**
 * Контроллер для управления пользователями.
 * Предоставляет методы для создания, обновления, удаления и аутентификации пользователей.
 */
public class UserController {

    private final UserService userService;
    private final UserConverter converter;

    public UserController(UserService userService) {
        this.converter = new UserConverter();
        this.userService = userService;
    }

    /**
     * Создает нового пользователя.
     *
     * @param createRequest запрос на создание пользователя.
     * @return созданный пользователь.
     * @throws IllegalArgumentException если данные в запросе некорректны.
     */
    public User create(CreateUserRequest createRequest) {
        validCreateUserRequest(createRequest);
        User user = converter.createUserRequestConvertToUser(createRequest);
        return userService.create(user);
    }

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param updateRequest запрос на обновление данных пользователя.
     * @param id            идентификатор пользователя.
     * @return обновленный пользователь.
     * @throws IllegalArgumentException если данные в запросе некорректны.
     */
    public User update(UpdateUserRequest updateRequest, long id) {
        validUpdateUserRequest(updateRequest);
        User user = converter.updateUserRequestConvertToUser(updateRequest);
        return userService.update(user, id);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     */
    public void delete(Long id) {
        userService.delete(id);
    }

    /**
     * Аутентифицирует пользователя.
     *
     * @param loginRequest запрос на вход в систему.
     * @return аутентифицированный пользователь.
     * @throws IllegalArgumentException если данные в запросе некорректны.
     */
    public User authenticateUser(LoginUserRequest loginRequest) {
        validLoginUserRequest(loginRequest);
        User user = converter.loginUserRequestConvertToUser(loginRequest);
        return userService.authenticateUser(user);
    }

    /**
     * Проверяет корректность запроса на создание пользователя.
     *
     * @param createRequest запрос на создание пользователя.
     * @throws IllegalArgumentException если данные в запросе некорректны.
     */
    private void validCreateUserRequest(CreateUserRequest createRequest) {
        if (createRequest.getName() == null || createRequest.getName().isEmpty())
            throw new IllegalArgumentException("Имя не может быть пустым.");
        validEmail(createRequest.getEmail());
        validPassword(createRequest.getPassword());
    }

    /**
     * Проверяет корректность запроса на обновление пользователя.
     *
     * @param updateRequest запрос на обновление пользователя.
     * @throws IllegalArgumentException если данные в запросе некорректны.
     */
    private void validUpdateUserRequest(UpdateUserRequest updateRequest) {
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
            validEmail(updateRequest.getEmail());
        }
    }

    /**
     * Проверяет корректность запроса на аутентификацию пользователя.
     *
     * @param loginRequest запрос на вход в систему.
     * @throws IllegalArgumentException если данные в запросе некорректны.
     */
    private void validLoginUserRequest(LoginUserRequest loginRequest) {
        validEmail(loginRequest.getEmail());
        validPassword(loginRequest.getPassword());
    }

    /**
     * Проверяет корректность email.
     *
     * @param email email пользователя.
     * @throws IllegalArgumentException если email некорректен.
     */
    private void validEmail(String email) {
        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Почта не может быть пустой.");
        if (!email.contains("@"))
            throw new IllegalArgumentException("Почта должна содержать '@'.");
    }

    /**
     * Проверяет корректность пароля.
     *
     * @param password пароль пользователя.
     * @throws IllegalArgumentException если пароль некорректен.
     */
    private void validPassword(String password) {
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Пароль не может быть пустой.");
    }
}
