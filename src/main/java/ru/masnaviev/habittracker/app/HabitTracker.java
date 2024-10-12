package ru.masnaviev.habittracker.app;

import ru.masnaviev.habittracker.handlers.HabitInputHandler;
import ru.masnaviev.habittracker.handlers.UserInputHandler;
import ru.masnaviev.habittracker.model.Session;

import static ru.masnaviev.habittracker.app.ConsoleView.*;
import static ru.masnaviev.habittracker.util.InputHandler.getUserInputInt;

public class HabitTracker {
    private final UserInputHandler userInputHandler;
    private final HabitInputHandler habitInputHandler;
    private final Session session;

    public HabitTracker() {
        this.session = new Session();
        this.userInputHandler = new UserInputHandler(session);
        this.habitInputHandler = new HabitInputHandler(session);
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
                    case 5 -> viewNotifications();
                    case 6 -> running = false;
                    default -> displayIncorrectChoice();
                }
            }
        }
    }

    private void trackingHabits() {
        ConsoleView.displayTrackingMenu();
        switch (getUserInputInt()) {
            case 1 -> habitInputHandler.addHabitExecution();
            case 2 -> habitInputHandler.getHabits();
            default -> displayIncorrectChoice();
        }
    }

    private void habitManagement() {
        ConsoleView.displayHabitMenu();
        switch (getUserInputInt()) {
            case 1 -> habitInputHandler.create();
            case 2 -> habitInputHandler.update();
            case 3 -> habitInputHandler.delete();
            case 4 -> habitInputHandler.getAll();
            default -> displayIncorrectChoice();
        }
    }


    private void accountManagement() {
        ConsoleView.displayAccountMenu();
        switch (getUserInputInt()) {
            case 1 -> userInputHandler.update();
            case 2 -> userInputHandler.delete();
            case 3 -> System.out.println("Функциональность еще не добавлена");
            default -> displayIncorrectChoice();
        }
    }

    private void viewNotifications() {
        System.out.println("Функциональность еще не добавлена");
    }

}
