package ru.masnaviev.habittracker.handlers;

import ru.masnaviev.habittracker.controllers.HabitController;
import ru.masnaviev.habittracker.in.dto.CreateHabitRequest;
import ru.masnaviev.habittracker.in.dto.UpdateHabitRequest;
import ru.masnaviev.habittracker.model.Frequency;
import ru.masnaviev.habittracker.model.Habit;
import ru.masnaviev.habittracker.model.Session;
import ru.masnaviev.habittracker.model.TimePeriod;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static java.lang.System.out;
import static ru.masnaviev.habittracker.util.InputHandler.getUserInputInt;
import static ru.masnaviev.habittracker.util.InputHandler.getUserInputString;

public class HabitInputHandler {
    private final HabitController habitController;
    private final Session session;

    public HabitInputHandler(Session session) {
        habitController = new HabitController();
        this.session = session;
    }

    public void create() {
        CreateHabitRequest createRequest = new CreateHabitRequest();

        out.println("Введите название");
        createRequest.setTitle(getUserInputString());
        out.println("Введите описание");
        createRequest.setDescription(getUserInputString());
        out.println("""
                Введите частоту:
                1. Ежедневно
                2. Еженедельно
                """);
        // TODO проверка ввода
        switch (getUserInputInt()) {
            case 1 -> createRequest.setFrequency(Frequency.DAILY);
            case 2 -> createRequest.setFrequency(Frequency.WEEKLY);
        }

        try {
            habitController.create(createRequest, session.getUser().getId());
            out.println("Привычка успешно создана.");
        } catch (Exception e) {
            out.println("Ошибка: " + e.getMessage());
        }
    }

    public void getAll() {
        List<Habit> habits = habitController.getAll(session.getUser().getId());
        if (!habits.isEmpty()) {
            out.println("""
                    Выберите поле для сортировки:
                    1. По дате создания
                    2. По статусу
                    """);
            //TODO /\
            switch (getUserInputInt()) {
                case 1 -> habits.sort((o1, o2) -> {
                    if (o1.getCreated().isAfter(o2.getCreated())) return -1;
                    if (o1.getCreated().equals(o2.getCreated())) return 0;
                    return 1;
                });
                case 2 -> habits.sort(Comparator.comparing(Habit::getFrequency));

            }
            displayHabits(habits);
        } else {
            out.println("У вас нет привычек");
        }
    }

    public void update() {
        List<Habit> habitsOptional = habitController.getAll(session.getUser().getId());

        if (!habitsOptional.isEmpty()) {
            displayHabits(habitsOptional);
            out.println("Выберите id привычки");

            long habitId = getUserInputInt();
            UpdateHabitRequest updateRequest = createUpdateHabitRequest(habitId);
            try {
                habitController.update(updateRequest, habitId);
                out.println("Привычка успешно обновлена.");
            } catch (Exception e) {
                out.println("Ошибка: " + e.getMessage());
            }
        } else {
            out.println("У вас нет привычек");
        }
    }

    public void delete() {
        List<Habit> habitsOptional = habitController.getAll(session.getUser().getId());
        if (!habitsOptional.isEmpty()) {
            displayHabits(habitsOptional);
            out.println("Выберите id привычки");

            long habitId = getUserInputInt();
            try {
                habitController.delete(habitId);
                out.println("Привычка успешно удалена.");
            } catch (Exception e) {
                out.println("Ошибка: " + e.getMessage());
            }
        } else {
            out.println("У вас нет привычек");
        }
    }

    public void addHabitExecution() {
        List<Habit> habitsOptional = habitController.getAll(session.getUser().getId());
        if (!habitsOptional.isEmpty()) {
            displayHabits(habitsOptional);
            out.println("Выберите id привычки");

            long habitId = getUserInputInt();
            try {
                habitController.addHabitExecution(habitId);
                out.println("Выполнение привычки успешно добавлено");
            } catch (Exception e) {
                out.println("Ошибка: " + e.getMessage());
            }
        } else {
            out.println("У вас нет привычек");
        }
    }

    public void getStatistic() {
        List<Habit> habitsOptional = habitController.getAll(session.getUser().getId());
        if (!habitsOptional.isEmpty()) {
            displayHabits(habitsOptional);
            out.println("Выберите id привычки");

            long habitId = getUserInputInt();

            out.println("Выберите период для отображения");
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
            try {
                List<LocalDate> habitExecutions = habitController.getStatistic(habitId, timePeriod);
                out.println("Статистика за указанный срок (дни выполнения)");
                out.println(habitExecutions);
            } catch (Exception e) {
                out.println("Ошибка: " + e.getMessage());
            }
        } else {
            out.println("У вас нет привычек");
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
}
