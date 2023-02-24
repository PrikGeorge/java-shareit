package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
public interface UserService {
    User getById(Long id);

    List<UserDTO> getAll();

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
}
