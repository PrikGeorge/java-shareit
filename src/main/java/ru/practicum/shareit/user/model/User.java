package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private Long id;

    private String name;

    @Email(message = "Email is incorrect")
    private String email;
}
