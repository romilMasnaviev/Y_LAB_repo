package ru.masnaviev.habittracker.handlers;

import ru.masnaviev.habittracker.controllers.AdminController;
import ru.masnaviev.habittracker.model.Habit;
import ru.masnaviev.habittracker.model.Session;
import ru.masnaviev.habittracker.model.User;

import java.util.List;

import static java.lang.System.out;
import static ru.masnaviev.habittracker.util.InputHandler.getUserInputInt;

public class AdminInputHandler {

    private final AdminController adminController;
    private final Session session;

    public AdminInputHandler(Session session, AdminController adminController) {
        this.adminController = adminController;
        this.session = session;
    }

    public void viewAllUsers() {
        try {
            List<User> users = adminController.getAllUsers(session);
            if (users.isEmpty()) {
                out.println("Нет зарегистрированных пользователей.");
            } else {
                out.println("Список пользователей:");
                for (User user : users) {
                    out.println(user);
                }
            }
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }

    }

    public void blockUser() {
        long userId = promptForUserId("Введите ID пользователя для блокировки:");
        executeAction(() -> {
            adminController.blockUser(userId, session);
            out.println("Пользователь успешно заблокирован.");
        });
    }

    public void unblockUser() {
        long userId = promptForUserId("Введите ID пользователя для разблокировки:");
        executeAction(() -> {
            adminController.unblockUser(userId, session);
            out.println("Пользователь успешно разблокирован.");
        });
    }

    public void deleteUser() {
        long userId = promptForUserId("Введите ID пользователя для удаления:");
        executeAction(() -> {
            adminController.deleteUser(userId, session);
            out.println("Пользователь успешно удален.");
        });
    }

    public void viewAllHabits() {
        List<Habit> habits = adminController.getAllHabits(session);
        if (habits.isEmpty()) {
            out.println("Нет зарегистрированных привычек.");
        } else {
            out.println("Список привычек:");
            for (Habit habit : habits) {
                out.println(habit);
            }
        }
    }

    public void deleteHabit() {
        long habitId = promptForUserId("Введите ID привычки для удаления:");
        executeAction(() -> {
            adminController.deleteHabit(habitId, session);
            out.println("Привычка успешно удалена.");
        });
    }


    private long promptForUserId(String prompt) {
        out.println(prompt);
        return getUserInputInt();
    }

    private void executeAction(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }
    }
}
