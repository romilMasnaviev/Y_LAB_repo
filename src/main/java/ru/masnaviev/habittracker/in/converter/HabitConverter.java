package ru.masnaviev.habittracker.in.converter;

import ru.masnaviev.habittracker.in.dto.CreateHabitRequest;
import ru.masnaviev.habittracker.in.dto.UpdateHabitRequest;
import ru.masnaviev.habittracker.model.Habit;

public class HabitConverter {

    public Habit createHabitRequestConvertToHabit(CreateHabitRequest createRequest) {
        return new Habit(createRequest.getTitle(), createRequest.getDescription(), createRequest.getFrequency());
    }

    public Habit updateHabitRequestConvertToHabit(UpdateHabitRequest updateRequest) {
        return new Habit(updateRequest.getTitle(), updateRequest.getDescription(), updateRequest.getFrequency());
    }
}
