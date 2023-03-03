package ru.practicum.shareit.repositorytests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTests {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    private ItemDTO itemDTO = ItemDTO.builder()
            .name("name")
            .description("description")
            .available(true)
            .build();

    private UserDTO userDTO = UserDTO.builder()
            .name("name")
            .email("user@email.com")
            .build();

    @Test
    void createTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(1L, itemDTO);
        assertEquals(item.getId(), itemController.getById(item.getId(), user.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.create(userDTO);
        itemController.create(1L, itemDTO);
        ItemDTO item = itemDTO.toBuilder().description("updateDescription").build();
        itemController.update(item, 1L, 1L);
        assertEquals(item.getDescription(), itemController.getById(1L, 1L).getDescription());
    }

    @Test
    void deleteTest() {
        userController.create(userDTO);
        itemController.create(1L, itemDTO);
        assertEquals(1, itemController.getAll(1L).size());
        itemController.delete(1L);
        assertEquals(0, itemController.getAll(1L).size());
    }

    @Test
    void searchTest() {
        userController.create(userDTO);
        itemController.create(1L, itemDTO);
        assertEquals(1, itemController.search("Desc").size());
    }

    @Test
    void createCommentTest() {
        CommentDTO comment = CommentDTO.builder().text("first comment").build();
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(1L, itemDTO);
        UserDTO user2 = userController.create(userDTO.toBuilder().email("email2@mail.com").build());
        bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2023, 2, 20, 12, 15))
                .end(LocalDateTime.of(2023, 2, 27, 12, 15))
                .itemId(item.getId()).build(), user2.getId());
        bookingController.approve(1L, 1L, true);
        itemController.createComment(item.getId(), user2.getId(), comment);
        assertEquals(1, itemController.getById(1L, 1L).getComments().size());
    }
}
