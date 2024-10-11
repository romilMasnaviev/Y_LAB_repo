package ru.masnaviev.habittracker.model;

import java.time.LocalDate;
import java.util.List;

public class StatisticEntity {
    private long habitId;
    private List<LocalDate> habitExecutions;
    private LocalDate created;
    private long currentStreak;
    private long longestStreak;
    private int executionRate;
}
