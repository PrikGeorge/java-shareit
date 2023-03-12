package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public Object getUsers() {
        log.info("Get all users");
        return userClient.getUsers();
    }

    @GetMapping("/{id}")
    public Object getUser(@PathVariable Long id) {
        log.info("Get user {}", id);
        return userClient.getUser(id);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid UserRequestDto requestDto) {
        log.info("Creating user");
        return userClient.createUser(requestDto);
    }

    @PatchMapping("/{id}")
    public Object updateUser(@RequestBody UserRequestDto requestDto,
                             @PathVariable Long id) {
        log.info("Update user {}", id);
        return userClient.updateUser(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public Object deleteUser(@PathVariable Long id) {
        log.info("Delete user {}", id);
        return userClient.deleteUser(id);
    }
}
