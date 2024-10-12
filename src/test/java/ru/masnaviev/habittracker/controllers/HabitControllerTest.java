package ru.masnaviev.habittracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.masnaviev.habittracker.controllers.util.StatisticEntity;
import ru.masnaviev.habittracker.controllers.util.TimePeriod;
import ru.masnaviev.habittracker.in.converter.HabitConverter;
import ru.masnaviev.habittracker.in.dto.CreateHabitRequest;
import ru.masnaviev.habittracker.in.dto.UpdateHabitRequest;
import ru.masnaviev.habittracker.model.Frequency;
import ru.masnaviev.habittracker.model.Habit;
import ru.masnaviev.habittracker.service.HabitService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HabitControllerTest {

    @Mock
    private HabitService habitService;

    @Mock
    private HabitConverter habitConverter;

    @InjectMocks
    private HabitController habitController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() {
        CreateHabitRequest request = new CreateHabitRequest("Test Habit", "Description", Frequency.DAILY);
        Habit habit = new Habit("Test Habit", "Description", Frequency.DAILY);
        long userId = 1L;

        when(habitConverter.createHabitRequestConvertToHabit(request)).thenReturn(habit);
        when(habitService.create(any(), anyLong())).thenReturn(habit);

        Habit createdHabit = habitController.create(request, userId);

        assertEquals(habit, createdHabit);
    }

    @Test
    void testCreate_InvalidTitle() {
        CreateHabitRequest request = new CreateHabitRequest("", "Description", Frequency.DAILY);
        long userId = 1L;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> habitController.create(request, userId));

        assertEquals("Название не может быть пустым.", thrown.getMessage());
        verify(habitService, never()).create(any(Habit.class), eq(userId));
    }


    @Test
    void testCreate_InvalidDescription() {
        CreateHabitRequest request = new CreateHabitRequest("Test Habit", "", Frequency.DAILY);
        long userId = 1L;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> habitController.create(request, userId));

        assertEquals("Описание не может быть пустым.", thrown.getMessage());
        verify(habitService, never()).create(any(Habit.class), eq(userId));
    }

    @Test
    void testCreate_InvalidFrequency() {
        CreateHabitRequest request = new CreateHabitRequest("Test Habit", "Description", null);
        long userId = 1L;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> habitController.create(request, userId));

        assertEquals("Частота привычки не может быть пустой.", thrown.getMessage());
        verify(habitService, never()).create(any(Habit.class), eq(userId));
    }


    @Test
    void testUpdate_Success() {
        UpdateHabitRequest request = new UpdateHabitRequest("Updated Habit", "Updated Description", Frequency.WEEKLY);
        Habit habit = new Habit("Updated Habit", "Updated Description", Frequency.WEEKLY);
        long habitId = 1L;

        when(habitConverter.updateHabitRequestConvertToHabit(request)).thenReturn(habit);
        when(habitService.update(any(), anyLong())).thenReturn(habit);

        Habit updatedHabit = habitController.update(request, habitId);

        assertEquals(habit, updatedHabit);
        verify(habitService, times(1)).update(any(), anyLong());
    }

    @Test
    void testDelete_Success() {
        long habitId = 1L;

        habitController.delete(habitId);

        verify(habitService, times(1)).delete(habitId);
    }

    @Test
    void testGet_Success() {
        long habitId = 1L;
        Habit habit = new Habit("Test Habit", "Description", Frequency.DAILY);

        when(habitService.get(habitId)).thenReturn(habit);

        Habit retrievedHabit = habitController.get(habitId);

        assertEquals(habit, retrievedHabit);
        verify(habitService, times(1)).get(habitId);
    }

    @Test
    void testGetAll_Success() {
        long userId = 1L;
        Habit habit = new Habit("Test Habit", "Description", Frequency.DAILY);
        List<Habit> habits = Collections.singletonList(habit);

        when(habitService.getAll(userId)).thenReturn(habits);

        List<Habit> retrievedHabits = habitController.getAll(userId);

        assertEquals(habits, retrievedHabits);
        verify(habitService, times(1)).getAll(userId);
    }

    @Test
    void testAddHabitExecution_Success() {
        long habitId = 1L;

        habitController.addHabitExecution(habitId);

        verify(habitService, times(1)).addHabitExecution(habitId);
    }

    @Test
    void testGetExecutions_Success() {
        long habitId = 1L;
        TimePeriod timePeriod = TimePeriod.DAY;
        List<LocalDate> executionDates = List.of(LocalDate.now());

        when(habitService.getExecutions(habitId, timePeriod)).thenReturn(executionDates);

        List<LocalDate> retrievedDates = habitController.getExecutions(habitId, timePeriod);

        assertEquals(executionDates, retrievedDates);
        verify(habitService, times(1)).getExecutions(habitId, timePeriod);
    }

    @Test
    void testGetStatistic_Success() {
        long userId = 1L;
        TimePeriod timePeriod = TimePeriod.MONTH;
        StatisticEntity statisticEntity = new StatisticEntity();
        List<StatisticEntity> statistics = List.of(statisticEntity);

        when(habitService.getStatistic(userId, timePeriod)).thenReturn(statistics);

        List<StatisticEntity> retrievedStatistics = habitController.getStatistic(userId, timePeriod);

        assertEquals(statistics, retrievedStatistics);
        verify(habitService, times(1)).getStatistic(userId, timePeriod);
    }
}