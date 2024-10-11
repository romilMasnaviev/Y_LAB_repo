package ru.masnaviev.habittracker.in.converter;

import ru.masnaviev.habittracker.in.dto.CreateUserRequest;
import ru.masnaviev.habittracker.in.dto.LoginUserRequest;
import ru.masnaviev.habittracker.in.dto.UpdateUserRequest;
import ru.masnaviev.habittracker.model.User;

public class UserConverter {

    public User createUserRequestConvertToUser(CreateUserRequest createRequest) {
        return new User(createRequest.getEmail(), createRequest.getPassword(), createRequest.getName());
    }

    public User updateUserRequestConvertToUser(UpdateUserRequest updateRequest) {
        return new User(updateRequest.getEmail(), updateRequest.getPassword(), updateRequest.getName());
    }

    public User loginUserRequestConvertToUser(LoginUserRequest createRequest) {
        return new User(createRequest.getEmail(), createRequest.getPassword());
    }
}
