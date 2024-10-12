package ru.masnaviev.habittracker.in.dto;

import ru.masnaviev.habittracker.models.Frequency;

public class HabitCreateRequest {
    private String title;
    private String description;
    private Frequency frequency;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Frequency getFrequency() {
        return frequency;
    }
}
