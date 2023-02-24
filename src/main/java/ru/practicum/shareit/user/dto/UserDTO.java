package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
@Data
@Builder
public class UserDTO {
    private Long id;

    private String name;

    @Email(message = "Email is incorrect")
    @NotEmpty
    private String email;
}
