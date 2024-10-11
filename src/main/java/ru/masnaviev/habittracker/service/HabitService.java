package ru.masnaviev.habittracker.service;

import ru.masnaviev.habittracker.model.Habit;
import ru.masnaviev.habittracker.model.Status;
import ru.masnaviev.habittracker.model.TimePeriod;
import ru.masnaviev.habittracker.repositories.HabitRepository;
import ru.masnaviev.habittracker.repositories.InMemoryHabitRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService() {
        habitRepository = new InMemoryHabitRepository();
    }

    public Habit create(Habit habit, long id) {
        // TODO при добавлении Spring добавить проверку id пользователя (userRepository)
        habit.setUserId(id);

        return habitRepository.add(habit);
    }

    public Habit update(Habit habit, long id) {
        Habit existingHabit = get(id);
        copyNonNullFields(habit, existingHabit);
        return existingHabit;
    }

    public void delete(long id) {
        habitRepository.delete(id);
    }

    public Habit get(long id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Привычки с таким id не существует"));
    }

    public List<Habit> getAll(long userId) {
        return habitRepository.getAll(userId);
    }

    public void addHabitExecution(long id) {
        Habit habit = get(id);
        if (habit.getStatus().equals(Status.CREATED))
            habit.setStatus(Status.IN_PROGRESS);
        habit.getExecutionHistory().add(LocalDate.now());
    }

    public List<LocalDate> getStatistic(long id, TimePeriod timePeriod) {
        exists(id);
        LocalDate start;
        switch (timePeriod) {
            case DAY -> start = LocalDate.now().minusDays(1);
            case WEEK -> start = LocalDate.now().minusWeeks(1);
            default -> start = LocalDate.now().minusMonths(1);
        }
        return habitRepository.getStatistic(id, start, LocalDate.now());
    }

    private void exists(long id) {
        if (!habitRepository.exists(id)){
            throw new NoSuchElementException("Привычки с таким id не существует");
        }
    }

    private void copyNonNullFields(Habit source, Habit target) {
        if (source.getTitle() != null) {
            target.setTitle(source.getTitle());
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }
        if (source.getFrequency() != null) {
            target.setFrequency(source.getFrequency());
        }
    }
}
