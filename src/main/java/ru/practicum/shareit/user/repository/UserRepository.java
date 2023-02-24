package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
public interface UserRepository {
    List<User> getAll();

    User findById(Long id);

    User save(User user);

    void delete(User user);
}
