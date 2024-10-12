package ru.masnaviev.habittracker.handlers;

import ru.masnaviev.habittracker.controllers.UserController;
import ru.masnaviev.habittracker.in.dto.CreateUserRequest;
import ru.masnaviev.habittracker.in.dto.LoginUserRequest;
import ru.masnaviev.habittracker.in.dto.UpdateUserRequest;
import ru.masnaviev.habittracker.model.User;
import ru.masnaviev.habittracker.security.Session;

import static ru.masnaviev.habittracker.handlers.util.PromptsForHandlers.*;

/**
 * Класс UserInputHandler отвечает за обработку взаимодействия пользователя с системой
 * в процессе управления профилем пользователя: создания, обновления, удаления и входа в систему.
 */
public class UserInputHandler {
    private final UserController userController;
    private final Session session;

    public UserInputHandler(Session session, UserController controller) {
        this.session = session;
        this.userController = controller;
    }

    /**
     * Метод для создания нового пользователя.
     * Запрашивает у пользователя имя, почту и пароль,
     * создаёт запрос и передаёт его контроллеру.
     */
    public void create() {
        CreateUserRequest createRequest = new CreateUserRequest();

        createRequest.setName(promptForInput("Введите имя"));
        createRequest.setEmail(promptForInput("Введите почту"));
        createRequest.setPassword(promptForInput("Введите пароль"));
        executeAction(() -> userController.create(createRequest),
                "Пользователь успешно создан.");
    }

    /**
     * Метод для обновления текущего профиля пользователя.
     * Извлекает текущего пользователя из сессии и запрашивает обновлённые данные.
     * Если пользователь вводит пустое значение, остаются текущие значения полей.
     */
    public void update() {
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        User currentUser = session.getUser();

        updateRequest.setName(promptForUpdate("Введите имя", currentUser.getName()));
        updateRequest.setEmail(promptForUpdate("Введите почту", currentUser.getEmail()));
        updateRequest.setPassword(promptForUpdate("Введите пароль", currentUser.getPassword()));

        executeAction(() -> userController.update(updateRequest, currentUser.getId()),
                "Пользователь успешно обновлен.");
    }

    /**
     * Метод для удаления текущего пользователя.
     * Удаляет пользователя по идентификатору и очищает данные пользователя в сессии.
     */
    public void delete() {
        executeAction(() -> userController.delete(session.getUser().getId()),
                "Профиль успешно удален.");
        session.setUser(null);
    }

    /**
     * Метод для входа в систему.
     * Запрашивает у пользователя почту и пароль и проверяет аутентификацию.
     */
    public void login() {
        LoginUserRequest loginRequest = new LoginUserRequest();

        loginRequest.setEmail(promptForInput("Введите почту"));
        loginRequest.setPassword(promptForInput("Введите пароль"));

        executeAction(() -> session.setUser(userController.authenticateUser(loginRequest)),
                "Вход успешен.");
    }
}
