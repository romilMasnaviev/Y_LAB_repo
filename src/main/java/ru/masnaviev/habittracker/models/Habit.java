package ru.masnaviev.habittracker.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Habit {
    private final LocalDateTime created;
    private ArrayList<LocalDate> executionHistory;
    private long userId;
    private long id;
    private String title;
    private String description;
    private Frequency frequency;
    private Status status;

    public Habit() {
        created = LocalDateTime.now();
        executionHistory = new ArrayList<>();
    }

    public Habit(String title, String description, Frequency frequency) {
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        status = Status.CREATED;
        created = LocalDateTime.now();
        executionHistory = new ArrayList<>();
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public ArrayList<LocalDate> getExecutionHistory() {
        return executionHistory;
    }

    public void setExecutionHistory(ArrayList<LocalDate> executionHistory) {
        this.executionHistory = executionHistory;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    @Override
    public String toString() {
        return "Habit{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", frequency=" + frequency +
                ", created=" + created +
                ", executionHistory=" + executionHistory +
                ", status=" + status +
                '}';
    }
}
