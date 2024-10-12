package ru.masnaviev.habittracker.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.masnaviev.habittracker.controllers.util.StatisticEntity;
import ru.masnaviev.habittracker.controllers.util.TimePeriod;
import ru.masnaviev.habittracker.models.Frequency;
import ru.masnaviev.habittracker.models.Habit;
import ru.masnaviev.habittracker.models.Status;
import ru.masnaviev.habittracker.out.repositories.HabitRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HabitServiceTest {
    Habit habit1;
    Habit habit2;
    @Mock
    private HabitRepository habitRepository;
    @InjectMocks
    private HabitService habitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        habitService = new HabitService(habitRepository);

        habit1 = new Habit("Habit 1", "Description 1", Frequency.DAILY);
        habit1.setId(1);
        habit1.setExecutionHistory(new ArrayList<>(Arrays.asList(
                LocalDate.now().minusDays(1),
                LocalDate.now())));

        habit2 = new Habit("Habit 2", "Description 2", Frequency.WEEKLY);
        habit2.setId(2);
        habit2.setExecutionHistory(new ArrayList<>(Arrays.asList(

                LocalDate.now().minusDays(3),
                LocalDate.now().minusDays(10))));
    }


    @Test
    void testCreate_Success() {
        Habit habit = new Habit("test", "test", Frequency.DAILY);

        when(habitRepository.add(any(Habit.class))).thenReturn(habit);

        Habit createdHabit = habitService.create(habit, 0);

        assertEquals(habit, createdHabit);
        verify(habitRepository).add(habit);
        assertEquals(0, habit.getUserId());
    }

    @Test
    void testUpdate_Success() {
        Habit existingHabit = new Habit();
        existingHabit.setId(1L);
        existingHabit.setTitle("Old Habit");

        Habit updatedData = new Habit();
        updatedData.setTitle("Updated Habit");

        when(habitRepository.findById(1L)).thenReturn(java.util.Optional.of(existingHabit));

        Habit updatedHabit = habitService.update(updatedData, 1L);

        assertEquals("Updated Habit", updatedHabit.getTitle());
        verify(habitRepository).findById(1L);
    }

    @Test
    void testUpdate_NoSuchElement() {
        Habit updatedData = new Habit();
        updatedData.setTitle("Updated Habit");

        when(habitRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> habitService.update(updatedData, 1L));

        assertEquals("Привычки с таким id не существует", thrown.getMessage());
    }

    @Test
    void testDelete_Success() {
        long habitId = 1L;

        habitService.delete(habitId);

        verify(habitRepository).delete(habitId);
    }

    @Test
    void testGet_Success() {
        Habit expectedHabit = new Habit();
        expectedHabit.setId(1L);

        when(habitRepository.findById(1L)).thenReturn(java.util.Optional.of(expectedHabit));

        Habit habit = habitService.get(1L);

        assertEquals(expectedHabit, habit);
        verify(habitRepository).findById(1L);
    }

    @Test
    void testGet_NoSuchElement() {
        when(habitRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> habitService.get(1L));

        assertEquals("Привычки с таким id не существует", thrown.getMessage());
    }

    @Test
    void testGetAll_Success() {
        long userId = 1L;
        List<Habit> habits = new ArrayList<>();
        Habit habit = new Habit();
        habit.setUserId(userId);
        habits.add(habit);

        when(habitRepository.getAll(userId)).thenReturn(habits);

        List<Habit> result = habitService.getAll(userId);

        assertEquals(habits, result);
    }

    @Test
    void testGetAll_NoHabits() {
        long userId = 1L;

        when(habitRepository.getAll(userId)).thenReturn(Collections.emptyList());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> habitService.getAll(userId));

        assertEquals("Ваш список привычек пуст", thrown.getMessage());
    }

    @Test
    void testAddHabitExecution_Success() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setFrequency(Frequency.DAILY);
        habit.setStatus(Status.CREATED);
        habit.setExecutionHistory(new ArrayList<>());

        when(habitRepository.findById(1L)).thenReturn(java.util.Optional.of(habit));

        habitService.addHabitExecution(1L);

        assertEquals(Status.IN_PROGRESS, habit.getStatus());
        assertTrue(habit.getExecutionHistory().contains(LocalDate.now()));
        verify(habitRepository).findById(1L);
    }

    @Test
    void testAddHabitExecution_AlreadyExecutedToday() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setFrequency(Frequency.DAILY);
        habit.setExecutionHistory(new ArrayList<>(Collections.singleton(LocalDate.now())));

        when(habitRepository.findById(1L)).thenReturn(java.util.Optional.of(habit));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> habitService.addHabitExecution(1L));
        assertEquals("Привычка уже выполнена сегодня.", thrown.getMessage());
    }

    @Test
    void testGetStatistic_Success() {
        when(habitRepository.getAll(1)).thenReturn(Arrays.asList(habit1, habit2));

        List<StatisticEntity> statistics = habitService.getStatistic(1, TimePeriod.DAY);

        System.out.println(statistics);
        assertEquals(2, statistics.size());

        StatisticEntity stat1 = statistics.get(0);
        assertEquals(habit1.getId(), stat1.getHabitId());
        assertEquals(2, stat1.getHabitExecutions().size());


        StatisticEntity stat2 = statistics.get(1);
        assertEquals(habit2.getId(), stat2.getHabitId());
        assertEquals(0, stat2.getHabitExecutions().size());
        assertEquals(0, stat2.getCurrentStreak());
        assertEquals(0.0, stat2.getSuccessRate());
    }

    @Test
    void testGetStatistic_NoHabits() {
        when(habitRepository.getAll(2)).thenReturn(Collections.emptyList());

        List<StatisticEntity> statistics = habitService.getStatistic(2, TimePeriod.WEEK);

        assertTrue(statistics.isEmpty());
    }

    @Test
    void getExecutions_shouldReturnExecutionsForLastWeek_whenTimePeriodIsWeek() {
        long habitId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate lastWeekStart = today.minusWeeks(1);
        List<LocalDate> expectedExecutions = List.of(lastWeekStart.plusDays(1), lastWeekStart.plusDays(3));

        when(habitRepository.getStatistic(habitId, lastWeekStart, today)).thenReturn(expectedExecutions);
        when(habitRepository.exists(habitId)).thenReturn(true);

        List<LocalDate> actualExecutions = habitService.getExecutions(habitId, TimePeriod.WEEK);

        assertEquals(expectedExecutions, actualExecutions);
    }

    @Test
    void getExecutions_shouldReturnExecutionsForLastMonth_whenTimePeriodIsMonth() {
        long habitId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate lastMonthStart = today.minusMonths(1);
        List<LocalDate> expectedExecutions = List.of(lastMonthStart.plusDays(10), lastMonthStart.plusDays(20));

        when(habitRepository.getStatistic(habitId, lastMonthStart, today)).thenReturn(expectedExecutions);
        when(habitRepository.exists(habitId)).thenReturn(true);

        List<LocalDate> actualExecutions = habitService.getExecutions(habitId, TimePeriod.MONTH);

        assertEquals(expectedExecutions, actualExecutions);
    }
}

