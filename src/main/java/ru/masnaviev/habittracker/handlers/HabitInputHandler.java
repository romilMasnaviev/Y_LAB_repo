package ru.masnaviev.habittracker.handlers;

import ru.masnaviev.habittracker.controllers.HabitController;
import ru.masnaviev.habittracker.in.dto.CreateHabitRequest;
import ru.masnaviev.habittracker.in.dto.UpdateHabitRequest;
import ru.masnaviev.habittracker.model.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static java.lang.System.out;
import static ru.masnaviev.habittracker.util.InputHandler.getUserInputInt;
import static ru.masnaviev.habittracker.util.InputHandler.getUserInputString;

public class HabitInputHandler {
    private final HabitController habitController;
    private final Session session;

    public HabitInputHandler(Session session, HabitController habitController) {
        this.session = session;
        this.habitController = habitController;
    }

    public void create() {
        CreateHabitRequest createRequest = new CreateHabitRequest();
        createRequest.setTitle(promptForInput("Введите название"));
        createRequest.setDescription(promptForInput("Введите описание"));
        createRequest.setFrequency(promptForFrequency());

        executeAction(() -> habitController.create(createRequest, session.getUser().getId()),
                "Привычка успешно создана.");
    }

    public void getAll() {
        List<Habit> habits = fetchHabits();
        if (habits == null) return;
        out.println("""
                Выберите поле для сортировки:
                1. По дате создания
                2. По статусу
                """);
        switch (getUserInputInt()) {
            case 1 -> habits.sort(Comparator.comparing(Habit::getCreated).reversed());
            case 2 -> habits.sort(Comparator.comparing(Habit::getFrequency));
        }
        displayHabits(habits);
    }

    public void update() {
        List<Habit> habits = fetchHabits();
        if (habits == null) return;

        displayHabits(habits);
        out.println("Введите id привычки для обновления:");
        long habitId = getUserInputInt();

        UpdateHabitRequest updateRequest = createUpdateHabitRequest(habitId);

        try {
            habitController.update(updateRequest, habitId);
            out.println("Привычка успешно обновлена.");
        } catch (Exception e) {
            out.println("Ошибка при обновлении привычки: " + e.getMessage());
        }
    }

    public void delete() {
        List<Habit> habits = fetchHabits();
        if (habits == null) return;

        displayHabits(habits);
        out.println("Выберите id привычки");
        long habitId = getUserInputInt();

        executeAction(() -> habitController.delete(habitId), "Привычка успешно удалена.");
    }

    public void addHabitExecution() {
        List<Habit> habits = fetchHabits();
        if (habits == null) return;
        displayHabits(habits);
        out.println("Выберите id привычки");
        long habitId = getUserInputInt();
        executeAction(() -> habitController.addHabitExecution(habitId),
                "Выполнение привычки успешно добавлено");
    }

    public void getHabits() {
        out.println("Выберите id привычки");
        long habitId = getUserInputInt();
        TimePeriod timePeriod = promptForTimePeriod();

        executeAction(() -> {
            List<LocalDate> habitExecutions = habitController.getExecutions(habitId, timePeriod);
            out.println("Статистика за указанный срок (дни выполнения)");
            out.println(habitExecutions);
        });
    }

    public void viewStatistics() {

        out.println("Выберите период для отображения статистики");
        out.println("""
                1. День
                2. Неделя
                3. Месяц
                """);
        TimePeriod timePeriod;
        switch (getUserInputInt()) {
            case 1 -> timePeriod = TimePeriod.DAY;
            case 2 -> timePeriod = TimePeriod.WEEK;
            default -> timePeriod = TimePeriod.MONTH;
        }
        List<StatisticEntity> statistic = habitController.getStatistic(session.getUser().getId(), timePeriod);

        for (StatisticEntity entity : statistic) {
            out.println(entity);
        }
    }

    private void displayHabits(List<Habit> habits) {
        for (Habit habit : habits) {
            out.println(habit);
        }
    }

    private UpdateHabitRequest createUpdateHabitRequest(long habitId) {
        UpdateHabitRequest updateRequest = new UpdateHabitRequest();
        Habit oldHabit = habitController.get(habitId);
        out.println("Привычка для изменения : " + oldHabit);

        out.println("Введите название (или нажмите Enter чтобы оставить текущее)");
        String title = getUserInputString();
        updateRequest.setTitle(title.isEmpty() ? oldHabit.getTitle() : title);

        out.println("Введите описание (или нажмите Enter чтобы оставить текущее)");
        String description = getUserInputString();
        updateRequest.setDescription(description.isEmpty() ? oldHabit.getDescription() : description);

        out.println("""
                Введите частоту:
                1. Ежедневно
                2. Еженедельно
                """);
        switch (getUserInputInt()) {
            case 1 -> updateRequest.setFrequency(Frequency.DAILY);
            case 2 -> updateRequest.setFrequency(Frequency.WEEKLY);

        }
        return updateRequest;
    }

    private String promptForInput(String prompt) {
        out.println(prompt);
        return getUserInputString();
    }

    private Frequency promptForFrequency() {
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

    private void executeAction(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }
    }

    private void executeAction(Runnable action, String successMessage) {
        try {
            action.run();
            out.println(successMessage);
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }
    }

    private List<Habit> fetchHabits() {
        try {
            return habitController.getAll(session.getUser().getId());
        } catch (Exception e) {
            out.println("Ошибка при получении списка привычек: " + e.getMessage());
            return null;
        }
    }

    private TimePeriod promptForTimePeriod() {
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
}
