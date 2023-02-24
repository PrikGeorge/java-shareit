package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;
import ru.practicum.shareit.utils.IdentityGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryImpl userRepositoryImpl;

    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public User getById(Long id) {
        User user = userRepositoryImpl.findById(id);
        if (Objects.isNull(user)) {
            log.error("User with id " + id + " not found");
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepositoryImpl.getAll();
        List<UserDTO> result = new ArrayList<>();

        if (users.isEmpty()) {
            return result;
        }

        for (User userToDTO : users) {
            result.add(UserMapper.toDTO(userToDTO));
        }

        return result;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        validateEmailUniqueness(user.getEmail());

        if (Objects.isNull(user.getId())) {
            user.setId(IdentityGenerator.INSTANCE.generateId(User.class));
        }

        User createdUser = userRepositoryImpl.save(user);
        return UserMapper.toDTO(createdUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = getById(id);

        if (Objects.nonNull(userDTO.getEmail()) && !user.getEmail().equals(userDTO.getEmail())) {
            validateEmailUniqueness(userDTO.getEmail());
        }

        user.setName(Objects.requireNonNullElse(userDTO.getName(), user.getName()));
        user.setEmail(Objects.requireNonNullElse(userDTO.getEmail(), user.getEmail()));
        User updatedUser = userRepositoryImpl.save(user);
        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = getById(id);
        userRepositoryImpl.delete(user);
    }

    private void validateEmailUniqueness(String email) {
        List<User> users = userRepositoryImpl.getAll();
        for (User user : users) {
            if (email.equals(user.getEmail())) {
                log.error("User with email " + email + " already exists");
                throw new DuplicateEmailException("User with email " + email + " already exists");
            }
        }
    }
}
