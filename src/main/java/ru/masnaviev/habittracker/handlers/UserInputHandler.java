package ru.masnaviev.habittracker.handlers;

import ru.masnaviev.habittracker.controllers.UserController;
import ru.masnaviev.habittracker.in.dto.CreateUserRequest;
import ru.masnaviev.habittracker.in.dto.LoginUserRequest;
import ru.masnaviev.habittracker.in.dto.UpdateUserRequest;
import ru.masnaviev.habittracker.model.Session;
import ru.masnaviev.habittracker.model.User;

import static java.lang.System.out;
import static ru.masnaviev.habittracker.util.InputHandler.getUserInputString;

public class UserInputHandler {
    private final UserController userController;
    private final Session session;

    public UserInputHandler(Session session, UserController controller) {
        this.session = session;
        this.userController = controller;
    }

    public void create() {
        CreateUserRequest createRequest = new CreateUserRequest();

        createRequest.setName(promptForInput("Введите имя"));
        createRequest.setEmail(promptForInput("Введите почту"));
        createRequest.setPassword(promptForInput("Введите пароль"));
        executeAction(() -> userController.create(createRequest),
                "Пользователь успешно создан.");
    }

    public void update() {
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        User currentUser = session.getUser();

        updateRequest.setName(promptForUpdate("Введите имя", currentUser.getName()));
        updateRequest.setEmail(promptForUpdate("Введите почту", currentUser.getEmail()));
        updateRequest.setPassword(promptForUpdate("Введите пароль", currentUser.getPassword()));

        executeAction(() -> userController.update(updateRequest, currentUser.getId()),
                "Пользователь успешно обновлен.");

    }

    public void delete() {
        executeAction(() -> userController.delete(session.getUser().getId()),
                "Профиль успешно удален.");
        session.setUser(null);
    }

    public void login() {
        LoginUserRequest loginRequest = new LoginUserRequest();

        loginRequest.setEmail(promptForInput("Введите почту"));
        loginRequest.setPassword(promptForInput("Введите пароль"));

        executeAction(() -> session.setUser(userController.authenticateUser(loginRequest)),
                "Вход успешен.");
    }

    private String promptForInput(String prompt) {
        out.println(prompt);
        return getUserInputString();
    }

    private String promptForUpdate(String prompt, String currentValue) {
        out.println(prompt + " (или нажмите Enter, чтобы оставить текущее: " + currentValue + ")");
        String input = getUserInputString();
        return input.isEmpty() ? currentValue : input;
    }

    private void executeAction(Runnable action, String successMessage) {
        try {
            action.run();
            out.println(successMessage);
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }
    }
}
