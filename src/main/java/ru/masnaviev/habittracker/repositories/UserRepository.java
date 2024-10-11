package ru.masnaviev.habittracker.repositories;

import ru.masnaviev.habittracker.model.User;

import java.util.Optional;

public interface UserRepository {

    User add(User user);

    Optional<User> findByEmail(String email);

    boolean isExistsByEmail(String email);

    void delete(Long id);

    Optional<User> findById(long id);

}
