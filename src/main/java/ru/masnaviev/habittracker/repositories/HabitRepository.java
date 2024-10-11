package ru.masnaviev.habittracker.repositories;

import ru.masnaviev.habittracker.model.Habit;
import ru.masnaviev.habittracker.model.TimePeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HabitRepository {

    Habit add(Habit habit);

    List<Habit> getAll(long id);

    Habit get(long id);

    void delete(long id);

    Optional<Habit> findById(long id);

    List<LocalDate> getStatistic(long id, LocalDate start, LocalDate end);

    boolean exists(long id);
}
