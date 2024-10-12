package ru.masnaviev.habittracker.out.repositories;

import ru.masnaviev.habittracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User add(User user);

    Optional<User> findByEmail(String email);

    boolean isExistsByEmail(String email);

    void delete(Long id);

    Optional<User> findById(long id);

    List<User> getAll();
}
