package ru.masnaviev.habittracker.repositories;

import ru.masnaviev.habittracker.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private static long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        user.setId(id++);
        return users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.entrySet().stream()
                .filter(a ->
                        a.getValue().getEmail().equals(email)).findFirst()
                .map(Map.Entry::getValue);
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return users.entrySet().stream().anyMatch(a -> a.getValue().getEmail().equals(email));
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }
}
