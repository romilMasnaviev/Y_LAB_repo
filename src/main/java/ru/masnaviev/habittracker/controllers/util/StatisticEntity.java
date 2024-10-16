package ru.masnaviev.habittracker.controllers.util;

import java.time.LocalDate;
import java.util.List;

public class StatisticEntity {
    private long habitId;
    private List<LocalDate> habitExecutions;
    private long currentStreak;
    private double successRate;

    public long getHabitId() {
        return habitId;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public List<LocalDate> getHabitExecutions() {
        return habitExecutions;
    }

    public void setHabitExecutions(List<LocalDate> habitExecutions) {
        this.habitExecutions = habitExecutions;
    }

    public long getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(long currentStreak) {
        this.currentStreak = currentStreak;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    @Override
    public String toString() {
        return "StatisticEntity{" +
                "habitId=" + habitId +
                ", habitExecutions=" + habitExecutions +
                ", currentStreak=" + currentStreak +
                ", successRate=" + successRate +
                '}';
    }
}
