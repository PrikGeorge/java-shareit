package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.user.mapper.UserMapper.toDTO;
import static ru.practicum.shareit.user.mapper.UserMapper.toEntity;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> getAll() {
        List<UserDTO> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            users.add(toDTO(user));
        }

        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + id));

        return toDTO(user);
    }

    @Transactional
    @Override
    public UserDTO create(UserDTO userDTO) {
        User user = toEntity(userDTO);

        return toDTO(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Невозможно обновить данные пользователя. " +
                        "Не найден пользователь с id: " + id));

        Optional.ofNullable(userDTO.getEmail()).ifPresent(updatedUser::setEmail);
        Optional.ofNullable(userDTO.getName()).ifPresent(updatedUser::setName);

        return toDTO(userRepository.save(updatedUser));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
