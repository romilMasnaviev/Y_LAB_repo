package ru.masnaviev.habittracker.handlers;

import ru.masnaviev.habittracker.controllers.UserController;
import ru.masnaviev.habittracker.in.dto.CreateUserRequest;
import ru.masnaviev.habittracker.in.dto.LoginUserRequest;
import ru.masnaviev.habittracker.in.dto.UpdateUserRequest;
import ru.masnaviev.habittracker.model.Session;
import ru.masnaviev.habittracker.model.User;

import static ru.masnaviev.habittracker.util.InputHandler.getUserInputString;

public class UserInputHandler {
    private final UserController userController;
    private final Session session;

    public UserInputHandler(Session session) {
        userController = new UserController();
        this.session = session;
    }


    public void create() {
        CreateUserRequest createRequest = new CreateUserRequest();

        System.out.println("Введите имя");
        createRequest.setName(getUserInputString());
        System.out.println("Введите почту");
        createRequest.setEmail(getUserInputString());
        System.out.println("Введите пароль");
        createRequest.setPassword(getUserInputString());

        try {
            userController.create(createRequest);
            System.out.println("Пользователь успешно создан.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void update() {
        UpdateUserRequest updateRequest = new UpdateUserRequest();

        System.out.println("Текущий пользователь: " + session.getUser());

        System.out.println("Введите имя (или нажмите Enter чтобы оставить текущее)");
        String name = getUserInputString();
        updateRequest.setName(name.isEmpty() ? session.getUser().getName() : name);

        System.out.println("Введите почту (или нажмите Enter чтобы оставить текущее)");
        String email = getUserInputString();
        updateRequest.setEmail(email.isEmpty() ? session.getUser().getEmail() : email);

        System.out.println("Введите пароль (или нажмите Enter чтобы оставить текущее)");
        String password = getUserInputString();
        updateRequest.setPassword(password.isEmpty() ? session.getUser().getPassword() : password);

        try {
            User user = userController.update(updateRequest, session.getUser().getId());
            session.setUser(user);
            System.out.println("Пользователь успешно обновлен.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            update();
        }
    }

    public void delete() {
        try {
            userController.delete(session.getUser().getId());
            session.setUser(null);

            System.out.println("Профиль успешно удален.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void login() {
        LoginUserRequest loginRequest = new LoginUserRequest();

        System.out.println("Введите почту");
        loginRequest.setEmail(getUserInputString());
        System.out.println("Введите пароль");
        loginRequest.setPassword(getUserInputString());
        try {
            session.setUser(userController.authenticateUser(loginRequest));
            System.out.println("Вход успешен.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
