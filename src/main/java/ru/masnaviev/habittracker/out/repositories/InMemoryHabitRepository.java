package ru.masnaviev.habittracker.out.repositories;

import ru.masnaviev.habittracker.model.Habit;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryHabitRepository implements HabitRepository {

    private static long id = 0;
    private final Map<Long, Habit> habits = new HashMap<>();

    @Override
    public Habit add(Habit habit) {
        habit.setId(id++);
        return habits.put(habit.getId(), habit);
    }

    @Override
    public List<Habit> getAll(long userId) {
        return habits.values().stream()
                .filter(habit -> habit.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long habitId) {
        habits.remove(habitId);
    }

    @Override
    public Optional<Habit> findById(long id) {
        return Optional.ofNullable(habits.get(id));
    }

    @Override
    public List<LocalDate> getStatistic(long id, LocalDate start, LocalDate end) {
        List<LocalDate> executionHistory = habits.get(id).getExecutionHistory();
        return executionHistory.stream()
                .filter(a -> !a.isBefore(start))
                .filter(a -> !a.isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public boolean exists(long id) {
        return habits.containsKey(id);
    }

    @Override
    public List<Habit> getAll() {

        return new ArrayList<>(habits.values());
    }
}
