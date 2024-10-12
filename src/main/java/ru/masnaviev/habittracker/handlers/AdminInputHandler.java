package ru.masnaviev.habittracker.handlers;

import ru.masnaviev.habittracker.controllers.AdminController;
import ru.masnaviev.habittracker.model.Habit;
import ru.masnaviev.habittracker.model.User;
import ru.masnaviev.habittracker.security.Session;

import java.util.List;

import static java.lang.System.out;
import static ru.masnaviev.habittracker.handlers.util.PromptsForHandlers.executeAction;
import static ru.masnaviev.habittracker.handlers.util.PromptsForHandlers.promptForUserId;

/**
 * Обработчик ввода админа.
 * Обеспечивает взаимодействие админа с системой для управления пользователями и привычками.
 */
public class AdminInputHandler {

    private final AdminController adminController;
    private final Session session;

    public AdminInputHandler(Session session, AdminController adminController) {
        this.adminController = adminController;
        this.session = session;
    }

    /**
     * Отображает список всех пользователей в системе.
     * Если пользователей нет, выводит соответствующее сообщение.
     */
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

    /**
     * Блокирует пользователя по его идентификатору.
     * Идентификатор пользователя запрашивается у администратора.
     */
    public void blockUser() {
        long userId = promptForUserId("Введите ID пользователя для блокировки:");
        executeAction(() -> {
            adminController.blockUser(userId, session);
            out.println("Пользователь успешно заблокирован.");
        });
    }

    /**
     * Разблокирует пользователя по его идентификатору.
     * Идентификатор пользователя запрашивается у администратора.
     */
    public void unblockUser() {
        long userId = promptForUserId("Введите ID пользователя для разблокировки:");
        executeAction(() -> {
            adminController.unblockUser(userId, session);
            out.println("Пользователь успешно разблокирован.");
        });
    }

    /**
     * Удаляет пользователя по его идентификатору.
     * Идентификатор пользователя запрашивается у администратора.
     */
    public void deleteUser() {
        long userId = promptForUserId("Введите ID пользователя для удаления:");
        executeAction(() -> {
            adminController.deleteUser(userId, session);
            out.println("Пользователь успешно удален.");
        });
    }

    /**
     * Отображает список всех привычек в системе.
     * Если привычек нет, выводит соответствующее сообщение.
     */
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

    /**
     * Удаляет привычку по её идентификатору.
     * Идентификатор привычки запрашивается у администратора.
     */
    public void deleteHabit() {
        long habitId = promptForUserId("Введите ID привычки для удаления:");
        executeAction(() -> {
            adminController.deleteHabit(habitId, session);
            out.println("Привычка успешно удалена.");
        });
    }
}
