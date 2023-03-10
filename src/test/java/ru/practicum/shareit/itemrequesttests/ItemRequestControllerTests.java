package ru.practicum.shareit.itemrequesttests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestControllerTests {
    @Autowired
    private ItemRequestController itemRequestController;

    @Autowired
    private UserController userController;

    private ItemRequestDTO itemRequestDto;

    private UserDTO userDto;

    @BeforeEach
    void init() {
        itemRequestDto = ItemRequestDTO
                .builder()
                .description("item request description")
                .build();

        userDto = UserDTO
                .builder()
                .name("name")
                .email("user@email.com")
                .build();
    }

    @Test
    void createTest() {
        UserDTO user = userController.create(userDto);
        ItemRequestDTO itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(1L, itemRequestController.getById(itemRequest.getId(), user.getId()).getId());
    }

    @Test
    void createByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemRequestController.create(1L, itemRequestDto));
    }

    @Test
    void getAllByUserTest() {
        UserDTO user = userController.create(userDto);
        ItemRequestDTO itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(1, itemRequestController.getAllByUser(user.getId()).size());
    }

    @Test
    void getAllByUserWithWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemRequestController.getAllByUser(1L));
    }

    @Test
    void getAll() {
        UserDTO user = userController.create(userDto);
        ItemRequestDTO itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(0, itemRequestController.getAll(0, 10, user.getId()).size());
        UserDTO user2 = userController.create(userDto.toBuilder().email("user1@email.com").build());
        assertEquals(1, itemRequestController.getAll(0, 10, user2.getId()).size());
    }

    @Test
    void getAllByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemRequestController.getAll(0, 10, 1L));
    }

    @Test
    void getAllWithWrongFrom() {
        assertThrows(BadRequestException.class, () -> itemRequestController.getAll(-1, 10, 1L));
    }
}
