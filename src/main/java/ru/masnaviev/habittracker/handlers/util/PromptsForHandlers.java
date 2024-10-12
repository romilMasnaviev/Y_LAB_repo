package ru.masnaviev.habittracker.handlers.util;

import ru.masnaviev.habittracker.model.Frequency;
import ru.masnaviev.habittracker.controllers.util.TimePeriod;

import static java.lang.System.out;
import static ru.masnaviev.habittracker.handlers.util.InputHandler.getUserInputInt;
import static ru.masnaviev.habittracker.handlers.util.InputHandler.getUserInputString;

public class PromptsForHandlers {

    public static long promptForUserId(String prompt) {
        out.println(prompt);
        return getUserInputInt();
    }

    public static String promptForInput(String prompt) {
        out.println(prompt);
        return getUserInputString();
    }

    public static TimePeriod promptForTimePeriod() {
        out.println("""
                Выберите период для отображения:
                1. День
                2. Неделя
                3. Месяц
                """);
        return switch (getUserInputInt()) {
            case 1 -> TimePeriod.DAY;
            case 2 -> TimePeriod.WEEK;
            default -> TimePeriod.MONTH;
        };
    }

    public static Frequency promptForFrequency() {
        out.println("""
                Введите частоту:
                1. Ежедневно
                2. Еженедельно
                """);
        return switch (getUserInputInt()) {
            case 1 -> Frequency.DAILY;
            case 2 -> Frequency.WEEKLY;
            default -> null;
        };
    }

    public static String promptForUpdate(String prompt, String currentValue) {
        out.println(prompt + " (или нажмите Enter, чтобы оставить текущее: " + currentValue + ")");
        String input = getUserInputString();
        return input.isEmpty() ? currentValue : input;
    }

    public static void executeAction(Runnable action, String successMessage) {
        try {
            action.run();
            out.println(successMessage);
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }
    }

    public static void executeAction(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }
    }
}
