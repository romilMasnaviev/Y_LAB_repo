package ru.masnaviev.habittracker.controllers;

import ru.masnaviev.habittracker.model.Habit;
import ru.masnaviev.habittracker.model.Session;
import ru.masnaviev.habittracker.model.User;
import ru.masnaviev.habittracker.service.HabitService;
import ru.masnaviev.habittracker.service.UserService;

import java.util.List;

public class AdminController {

    private final UserService userService;
    private final HabitService habitService;

    public AdminController(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    public List<User> getAllUsers(Session session) {
        return userService.getAllUsers(session);
    }

    public void blockUser(long userId, Session session) {
        userService.blockUser(userId, session);
    }

    public void unblockUser(long userId, Session session) {
        userService.unblockUser(userId, session);
    }

    public void deleteUser(long userId, Session session) {
        userService.deleteUserByAdmin(userId, session);
    }

    public List<Habit> getAllHabits(Session session) {
        userService.checkAdminRights(session);
        return habitService.getAllHabits();
    }

    public void deleteHabit(long habitId, Session session) {
        userService.checkAdminRights(session);
        habitService.deleteHabit(habitId);
    }
}
