package ru.masnaviev.habittracker.in.dto;

import ru.masnaviev.habittracker.models.Frequency;

public class UpdateHabitRequest {
    private String title;
    private String description;
    private Frequency frequency;

    public UpdateHabitRequest() {
    }

    public UpdateHabitRequest(String title, String description, Frequency frequency) {
        this.title = title;
        this.description = description;
        this.frequency = frequency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }
}
