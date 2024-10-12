package ru.masnaviev.habittracker.services;

import ru.masnaviev.habittracker.controllers.util.StatisticEntity;
import ru.masnaviev.habittracker.controllers.util.TimePeriod;
import ru.masnaviev.habittracker.models.Frequency;
import ru.masnaviev.habittracker.models.Habit;
import ru.masnaviev.habittracker.models.Status;
import ru.masnaviev.habittracker.out.repositories.HabitRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Сервис для управления привычками пользователей.
 * <p>
 * Этот класс предоставляет методы для создания, обновления, удаления и получения привычек,
 * а также для отслеживания выполнения привычек и расчета статистики.
 */
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    /**
     * Создает новую привычку для пользователя.
     *
     * @param habit  Объект Habit, представляющий привычку, которую необходимо создать.
     * @param userId Идентификатор пользователя, которому принадлежит привычка.
     * @return Созданный объект Habit.
     */
    public Habit create(Habit habit, long userId) {
        habit.setUserId(userId);
        return habitRepository.add(habit);
    }

    /**
     * Обновляет существующую привычку.
     *
     * @param habit Объект Habit, содержащий обновленные данные.
     * @param id    Идентификатор привычки, которую нужно обновить.
     * @return Обновленный объект Habit.
     */
    public Habit update(Habit habit, long id) {
        Habit existingHabit = get(id);
        copyNonNullFields(habit, existingHabit);
        return existingHabit;
    }

    /**
     * Удаляет привычку по ее идентификатору.
     *
     * @param id Идентификатор привычки, которую необходимо удалить.
     */
    public void delete(long id) {
        habitRepository.delete(id);
    }

    /**
     * Получает привычку по ее идентификатору.
     *
     * @param id Идентификатор привычки.
     * @return Объект Habit, представляющий найденную привычку.
     * @throws NoSuchElementException если привычка с указанным идентификатором не существует.
     */
    public Habit get(long id) {
        return habitRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Привычки с таким id не существует"));
    }

    /**
     * Получает все привычки пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список привычек пользователя.
     * @throws NoSuchElementException если у пользователя нет привычек.
     */
    public List<Habit> getAll(long userId) {
        List<Habit> userHabits = habitRepository.getAll(userId);
        if (userHabits.isEmpty()) {
            throw new NoSuchElementException("Ваш список привычек пуст");
        }
        return userHabits;
    }

    /**
     * Добавляет выполнение привычки.
     *
     * @param id Идентификатор привычки.
     * @throws IllegalStateException если привычка уже выполнена сегодня или на этой неделе (в зависимости от частоты).
     */
    public void addHabitExecution(long id) {
        Habit habit = get(id);
        habitAlreadyCompleted(habit);
        if (habit.getStatus().equals(Status.CREATED)) {
            habit.setStatus(Status.IN_PROGRESS);
        }
        habit.getExecutionHistory().add(LocalDate.now());
    }

    /**
     * Получает даты выполнения привычки за указанный период.
     *
     * @param id         Идентификатор привычки.
     * @param timePeriod Период времени (день, неделя или месяц).
     * @return Список дат выполнения привычки.
     */
    public List<LocalDate> getExecutions(long id, TimePeriod timePeriod) {
        exists(id);
        LocalDate start;
        switch (timePeriod) {
            case DAY -> start = LocalDate.now().minusDays(1);
            case WEEK -> start = LocalDate.now().minusWeeks(1);
            default -> start = LocalDate.now().minusMonths(1);
        }
        return habitRepository.getStatistic(id, start, LocalDate.now());
    }

    /**
     * Получает статистику выполнения привычек пользователя за указанный период.
     *
     * @param userId     Идентификатор пользователя.
     * @param timePeriod Период времени (день, неделя или месяц).
     * @return Список статистики для привычек пользователя.
     */
    public List<StatisticEntity> getStatistic(long userId, TimePeriod timePeriod) {
        List<Habit> habits = habitRepository.getAll(userId);
        List<StatisticEntity> statistics = new ArrayList<>();
        for (Habit habit : habits) {
            StatisticEntity statistic = new StatisticEntity();
            statistic.setHabitId(habit.getId());

            List<LocalDate> executions = getExecutions(habit, timePeriod);
            statistic.setHabitExecutions(executions);
            statistic.setCurrentStreak(calculateStreak(executions, habit.getFrequency()));
            statistic.setSuccessRate(calculateSuccessRate(executions.size(), habit, timePeriod));
            statistics.add(statistic);
        }
        return statistics;
    }

    /**
     * Получает все привычки.
     *
     * @return Список всех привычек.
     */
    public List<Habit> getAllHabits() {
        return habitRepository.getAll();
    }

    /**
     * Удаляет привычку по ее идентификатору.
     *
     * @param habitId Идентификатор привычки, которую необходимо удалить.
     */
    public void deleteHabit(long habitId) {
        habitRepository.delete(habitId);
    }

    /**
     * Получает даты выполнения привычки за указанный период.
     *
     * @param habit      Привычка.
     * @param timePeriod Период времени (день, неделя или месяц).
     * @return Список дат выполнения привычки.
     */
    public List<LocalDate> getExecutions(Habit habit, TimePeriod timePeriod) {
        LocalDate start;
        switch (timePeriod) {
            case DAY -> start = LocalDate.now().minusDays(1);
            case WEEK -> start = LocalDate.now().minusWeeks(1);
            default -> start = LocalDate.now().minusMonths(1);
        }
        return habit.getExecutionHistory().stream().filter(
                a -> !a.isBefore(start)).filter(a -> !a.isAfter(LocalDate.now())).collect(Collectors.toList());
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

    private long calculateStreak(List<LocalDate> executions, Frequency frequency) {
        if (executions.isEmpty()) {
            return 0;
        }
        Collections.sort(executions);
        long streak = 0;
        LocalDate previousDate = executions.get(executions.size() - 1);
        for (int i = executions.size() - 1; i >= 0; i--) {
            LocalDate currentDate = executions.get(i);
            if (frequency == Frequency.DAILY) {
                if (previousDate.equals(currentDate) || previousDate.minusDays(1).equals(currentDate)) {
                    streak++;
                    previousDate = currentDate;
                } else break;
            } else if (frequency == Frequency.WEEKLY) {
                if (previousDate.minusWeeks(1).equals(currentDate)) {
                    streak++;
                    previousDate = currentDate;
                } else break;
            }
        }
        return streak;
    }

    private double calculateSuccessRate(int executionsCount, Habit habit, TimePeriod timePeriod) {
        long totalDays;
        switch (timePeriod) {
            case DAY -> totalDays = 1;
            case WEEK -> totalDays = 7;
            default -> totalDays = LocalDate.now().lengthOfMonth();
        }
        if (habit.getFrequency() == Frequency.DAILY) {
            return totalDays > 0 ? (double) executionsCount / totalDays * 100 : 0;
        } else if (habit.getFrequency() == Frequency.WEEKLY) {
            long weeksInPeriod = totalDays / 7;
            return weeksInPeriod > 0 ? (double) executionsCount / weeksInPeriod * 100 : 0;
        }
        return 0;
    }

    private void exists(long id) {
        if (!habitRepository.exists(id)) {
            throw new NoSuchElementException("Привычки с таким id не существует");
        }
    }

    private void habitAlreadyCompleted(Habit habit) {
        if (habit.getFrequency() == Frequency.DAILY && habit.getExecutionHistory().contains(LocalDate.now())) {
            throw new IllegalStateException("Привычка уже выполнена сегодня.");
        } else if (habit.getFrequency() == Frequency.WEEKLY && habit.getExecutionHistory().stream().anyMatch(
                date -> date.isAfter(LocalDate.now().minusDays(7)))) {
            throw new IllegalStateException("Привычка уже выполнена на этой неделе.");
        }
    }
}
