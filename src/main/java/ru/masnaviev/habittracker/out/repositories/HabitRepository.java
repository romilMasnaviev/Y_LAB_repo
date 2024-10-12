package ru.masnaviev.habittracker.out.repositories;

import ru.masnaviev.habittracker.model.Habit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitRepository {

    Habit add(Habit habit);

    List<Habit> getAll(long id);

    void delete(long id);

    Optional<Habit> findById(long id);

    List<LocalDate> getStatistic(long id, LocalDate start, LocalDate end);

    boolean exists(long id);

    List<Habit> getAll();
}
