package ru.masnaviev.habittracker.controllers;

import ru.masnaviev.habittracker.models.Habit;
import ru.masnaviev.habittracker.models.User;
import ru.masnaviev.habittracker.security.Session;
import ru.masnaviev.habittracker.services.HabitService;
import ru.masnaviev.habittracker.services.UserService;

import java.util.List;

/**
 * Контроллер для управления функциями администратора, включая управление пользователями и привычками.
 */
public class AdminController {

    private final UserService userService;
    private final HabitService habitService;

    public AdminController(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    /**
     * Получает список всех пользователей.
     *
     * @param session текущая сессия пользователя (должна быть с правами администратора)
     * @return список всех пользователей
     * @throws SecurityException если у текущего пользователя нет прав администратора
     */
    public List<User> getAllUsers(Session session) {
        userService.checkAdminRights(session);
        return userService.getAllUsers();
    }

    /**
     * Блокирует пользователя по его идентификатору.
     *
     * @param userId  идентификатор пользователя, которого нужно заблокировать
     * @param session текущая сессия пользователя (должна быть с правами администратора)
     * @throws SecurityException если у текущего пользователя нет прав администратора
     */
    public void blockUser(long userId, Session session) {
        userService.checkAdminRights(session);
        userService.blockUser(userId);
    }

    /**
     * Разблокирует пользователя по его идентификатору.
     *
     * @param userId  идентификатор пользователя, которого нужно разблокировать
     * @param session текущая сессия пользователя (должна быть с правами администратора)
     * @throws SecurityException если у текущего пользователя нет прав администратора
     */
    public void unblockUser(long userId, Session session) {
        userService.checkAdminRights(session);
        userService.unblockUser(userId);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId  идентификатор пользователя, которого нужно удалить
     * @param session текущая сессия пользователя (должна быть с правами администратора)
     * @throws SecurityException если у текущего пользователя нет прав администратора
     */
    public void deleteUser(long userId, Session session) {
        userService.checkAdminRights(session);
        userService.deleteUserByAdmin(userId);
    }

    /**
     * Получает список всех привычек.
     *
     * @param session текущая сессия пользователя (должна быть с правами администратора)
     * @return список всех привычек
     * @throws SecurityException если у текущего пользователя нет прав администратора
     */
    public List<Habit> getAllHabits(Session session) {
        userService.checkAdminRights(session);
        return habitService.getAllHabits();
    }

    /**
     * Удаляет привычку по её идентификатору.
     *
     * @param habitId идентификатор привычки, которую нужно удалить
     * @param session текущая сессия пользователя (должна быть с правами администратора)
     * @throws SecurityException если у текущего пользователя нет прав администратора
     */
    public void deleteHabit(long habitId, Session session) {
        userService.checkAdminRights(session);
        habitService.deleteHabit(habitId);
    }
}
