package com.example.bankapi.repos;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.bankapi.models.User;

@Repository
public class UserRepo {
    private final List<User> users = new ArrayList<>();
    public UserRepo() {
        users.add(new User(1, "user1", "password1"));
        users.add(new User(2, "user2", "password2"));
        users.add(new User(3, "user3", "password3"));
        users.add(new User(4, "admin", "adminpass"));
    }

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> getUserById(Integer id) {
        List<User> users = getUsers();
        return users.stream().filter(u -> u.getId() == id).findFirst();
    }

    public User createUser(User user) {
        user.setId(users.size() + 1);
        users.add(user);
        return user;
    }

    public void deleteUser(int id) {
        users.removeIf(u -> u.getId() == id);
    }
    public User updateUser(int id, User updatedUser) {
            User existingCustomer = getUserById(id).get();
            if (updatedUser.getUsername() != null) {
                existingCustomer.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getPassword() != null) {
                existingCustomer.setPassword(updatedUser.getPassword());
            }
            return existingCustomer;
    }
}
