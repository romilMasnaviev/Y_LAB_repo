package ru.masnaviev.habittracker.security;

import ru.masnaviev.habittracker.models.Role;
import ru.masnaviev.habittracker.models.User;

public class Session {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void logout() {
        user = null;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public boolean isAdmin() {
        return user.getRole().equals(Role.ADMIN);
    }
}
