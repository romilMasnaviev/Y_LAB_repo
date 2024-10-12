package ru.masnaviev.habittracker.controllers;

import ru.masnaviev.habittracker.in.converter.HabitConverter;
import ru.masnaviev.habittracker.in.dto.CreateHabitRequest;
import ru.masnaviev.habittracker.in.dto.UpdateHabitRequest;
import ru.masnaviev.habittracker.model.Habit;
import ru.masnaviev.habittracker.model.StatisticEntity;
import ru.masnaviev.habittracker.model.TimePeriod;
import ru.masnaviev.habittracker.service.HabitService;

import java.time.LocalDate;
import java.util.List;

public class HabitController {

    private final HabitService habitService;
    private final HabitConverter habitConverter;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
        this.habitConverter = new HabitConverter();
    }

    public Habit create(CreateHabitRequest createRequest, long userId) {
        validCreateHabitRequest(createRequest);
        Habit habit = habitConverter.createHabitRequestConvertToHabit(createRequest);
        return habitService.create(habit, userId);
    }

    public Habit update(UpdateHabitRequest updateRequest, long id) {
        Habit habit = habitConverter.updateHabitRequestConvertToHabit(updateRequest);
        return habitService.update(habit, id);
    }

    public void delete(long id) {
        habitService.delete(id);
    }

    public Habit get(long id) {
        return habitService.get(id);
    }

    public List<Habit> getAll(long userId) {
        return habitService.getAll(userId);
    }

    public void addHabitExecution(long id) {
        habitService.addHabitExecution(id);
    }

    public List<LocalDate> getExecutions(long habitId, TimePeriod timePeriod) {
        return habitService.getExecutions(habitId, timePeriod);
    }

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
