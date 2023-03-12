package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
@Getter
@Setter
@Builder(toBuilder = true)
public class UserDTO {
    private Long id;

    private String name;

    private String email;
}
