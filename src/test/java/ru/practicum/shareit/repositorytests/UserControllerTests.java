package ru.practicum.shareit.repositorytests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTests {
    @Autowired
    private UserController userController;

    private UserDTO user = UserDTO.builder()
            .name("name")
            .email("user@email.com")
            .build();

    @Test
    void createTest() {
        UserDTO userDTO = userController.create(user);
        assertEquals(userDTO.getId(), userController.getById(userDTO.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.create(user);
        UserDTO userDTO = user.toBuilder().email("update@email.com").build();
        userController.update(1L, userDTO);
        assertEquals(userDTO.getEmail(), userController.getById(1L).getEmail());
    }

    @Test
    void deleteTest() {
        UserDTO userDTO = userController.create(user);
        assertEquals(1, userController.getAll().size());
        userController.delete(userDTO.getId());
        assertEquals(0, userController.getAll().size());
    }
}
