package ru.masnaviev.habittracker.app;

import ru.masnaviev.habittracker.controllers.AdminController;
import ru.masnaviev.habittracker.controllers.HabitController;
import ru.masnaviev.habittracker.controllers.UserController;
import ru.masnaviev.habittracker.handlers.AdminInputHandler;
import ru.masnaviev.habittracker.handlers.HabitInputHandler;
import ru.masnaviev.habittracker.handlers.UserInputHandler;
import ru.masnaviev.habittracker.security.Session;
import ru.masnaviev.habittracker.service.HabitService;
import ru.masnaviev.habittracker.service.UserService;

import static ru.masnaviev.habittracker.app.util.ConsoleView.*;
import static ru.masnaviev.habittracker.handlers.util.InputHandler.getUserInputInt;

public class HabitTracker {
    private final UserInputHandler userInputHandler;
    private final HabitInputHandler habitInputHandler;
    private final AdminInputHandler adminInputHandler;
    private final Session session;

    public HabitTracker() {
        UserService userService = new UserService();
        HabitService habitService = new HabitService();

        HabitController habitController = new HabitController(habitService);
        UserController userController = new UserController(userService);
        AdminController adminController = new AdminController(userService, habitService);

        this.session = new Session();
        this.userInputHandler = new UserInputHandler(session, userController);
        this.habitInputHandler = new HabitInputHandler(session, habitController);
        this.adminInputHandler = new AdminInputHandler(session, adminController);
    }

    public void start() {
        boolean running = true;
        while (running) {
            if (!session.isLoggedIn()) {
                displayLoginMenu();
                switch (getUserInputInt()) {
                    case 1 -> userInputHandler.create();
                    case 2 -> userInputHandler.login();
                    case 3 -> running = false;
                    default -> displayIncorrectChoice();
                }
            } else {
                displayMainMenu();
                switch (getUserInputInt()) {
                    case 1 -> accountManagement();
                    case 2 -> habitManagement();
                    case 3 -> trackingHabits();
                    case 4 -> habitInputHandler.viewStatistics();
                    case 5 -> displayNoImplementation();
                    case 6 -> administration();
                    case 7 -> logout();
                    case 8 -> running = false;
                    default -> displayIncorrectChoice();
                }
            }
        }
    }

    private void logout() {
        session.logout();
    }

    private void administration() {
        displayAdministrationMenu();
        switch (getUserInputInt()) {
            case 1 -> adminInputHandler.viewAllUsers();
            case 2 -> adminInputHandler.blockUser();
            case 3 -> adminInputHandler.unblockUser();
            case 4 -> adminInputHandler.deleteUser();
            case 5 -> adminInputHandler.viewAllHabits();
            case 6 -> adminInputHandler.deleteHabit();
            default -> displayIncorrectChoice();
        }
    }

    private void trackingHabits() {
        displayTrackingMenu();
        switch (getUserInputInt()) {
            case 1 -> habitInputHandler.addHabitExecution();
            case 2 -> habitInputHandler.getHabits();
            default -> displayIncorrectChoice();
        }
    }

    private void habitManagement() {
        displayHabitMenu();
        switch (getUserInputInt()) {
            case 1 -> habitInputHandler.create();
            case 2 -> habitInputHandler.update();
            case 3 -> habitInputHandler.delete();
            case 4 -> habitInputHandler.getAll();
            default -> displayIncorrectChoice();
        }
    }

    private void accountManagement() {
        displayAccountMenu();
        switch (getUserInputInt()) {
            case 1 -> userInputHandler.update();
            case 2 -> userInputHandler.delete();
            case 3 -> displayNoImplementation();
            default -> displayIncorrectChoice();
        }
    }
}
