package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */

public interface UserRepository extends JpaRepository<User, Long> {
}
