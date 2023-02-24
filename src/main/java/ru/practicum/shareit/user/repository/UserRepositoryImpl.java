package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */

@Repository
public class UserRepositoryImpl implements UserRepository{

    private final Map<Long, User> users = new HashMap<>();

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
    public User findById(Long id) {
        return users.get(id);
    }

    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void delete(User user) {
        users.remove(user.getId());
    }
}
