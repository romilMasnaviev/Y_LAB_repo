package ru.masnaviev.habittracker.controllers;

import ru.masnaviev.habittracker.controllers.util.StatisticEntity;
import ru.masnaviev.habittracker.controllers.util.TimePeriod;
import ru.masnaviev.habittracker.in.converter.HabitConverter;
import ru.masnaviev.habittracker.in.dto.CreateHabitRequest;
import ru.masnaviev.habittracker.in.dto.UpdateHabitRequest;
import ru.masnaviev.habittracker.models.Habit;
import ru.masnaviev.habittracker.services.HabitService;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для управления привычками пользователей.
 * Предоставляет методы для создания, обновления, удаления и получения привычек,
 * а также для работы с выполнением привычек и статистикой.
 */
public class HabitController {

    private final HabitService habitService;
    private final HabitConverter habitConverter;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
        this.habitConverter = new HabitConverter();
    }

    /**
     * Создает новую привычку для пользователя.
     *
     * @param createRequest запрос на создание привычки.
     * @param userId        идентификатор пользователя.
     * @return созданная привычка.
     * @throws IllegalArgumentException если данные в запросе некорректны.
     */
    public Habit create(CreateHabitRequest createRequest, long userId) {
        validCreateHabitRequest(createRequest);
        Habit habit = habitConverter.createHabitRequestConvertToHabit(createRequest);
        return habitService.create(habit, userId);
    }

    /**
     * Обновляет существующую привычку.
     *
     * @param updateRequest запрос на обновление привычки.
     * @param id            идентификатор привычки.
     * @return обновленная привычка.
     */
    public Habit update(UpdateHabitRequest updateRequest, long id) {
        Habit habit = habitConverter.updateHabitRequestConvertToHabit(updateRequest);
        return habitService.update(habit, id);
    }

    /**
     * Удаляет привычку по ее идентификатору.
     *
     * @param id идентификатор привычки.
     */
    public void delete(long id) {
        habitService.delete(id);
    }

    /**
     * Получает привычку по ее идентификатору.
     *
     * @param id идентификатор привычки.
     * @return привычка.
     */
    public Habit get(long id) {
        return habitService.get(id);
    }

    /**
     * Получает все привычки пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список привычек пользователя.
     */
    public List<Habit> getAll(long userId) {
        return habitService.getAll(userId);
    }

    /**
     * Добавляет выполнение привычки.
     *
     * @param id идентификатор привычки.
     */
    public void addHabitExecution(long id) {
        habitService.addHabitExecution(id);
    }

    /**
     * Получает выполнения привычки за указанный период времени.
     *
     * @param habitId    идентификатор привычки.
     * @param timePeriod период времени для получения выполнений.
     * @return список дат выполнений.
     */
    public List<LocalDate> getExecutions(long habitId, TimePeriod timePeriod) {
        return habitService.getExecutions(habitId, timePeriod);
    }

    /**
     * Получает статистику привычек пользователя за указанный период времени.
     *
     * @param userId     идентификатор пользователя.
     * @param timePeriod период времени для получения статистики.
     * @return список статистики.
     */
    public List<StatisticEntity> getStatistic(long userId, TimePeriod timePeriod) {
        return habitService.getStatistic(userId, timePeriod);
    }

    private void validCreateHabitRequest(CreateHabitRequest createRequest) {
        if (createRequest.getTitle() == null || createRequest.getTitle().isEmpty())
            throw new IllegalArgumentException("Название не может быть пустым.");
        if (createRequest.getDescription() == null || createRequest.getDescription().isEmpty())
            throw new IllegalArgumentException("Описание не может быть пустым.");
        if (createRequest.getFrequency() == null) {
            throw new IllegalArgumentException("Частота привычки не может быть пустой.");
        }
    }
}