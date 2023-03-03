package ru.practicum.shareit.itemtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingShortDTO;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTests {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private ItemRequestController itemRequestController;

    private ItemDTO itemDto;

    private UserDTO userDto;

    private ItemRequestDTO itemRequestDto;

    private CommentDTO comment;

    @BeforeEach
    void init() {
        itemDto = ItemDTO.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        userDto = UserDTO.builder()
                .name("name")
                .email("user@email.com")
                .build();

        itemRequestDto = ItemRequestDTO
                .builder()
                .description("item request description")
                .build();

        comment = CommentDTO
                .builder()
                .text("first comment")
                .build();
    }

    @Test
    void createTest() {
        UserDTO user = userController.create(userDto);
        ItemDTO item = itemController.create(1L, itemDto);
        assertEquals(item.getId(), itemController.getById(item.getId(), user.getId()).getId());
    }

    @Test
    void createWithRequestTest() {
        UserDTO user = userController.create(userDto);
        ItemRequestDTO itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        itemDto.setRequestId(1L);
        UserDTO user2 = userController.create(userDto.toBuilder().email("user2@email.com").build());
        ItemDTO item = itemController.create(2L, itemDto);
        assertEquals(item, itemController.getById(1L, 2L));
    }

    @Test
    void createByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemController.create(1L, itemDto));
    }

    @Test
    void createWithWrongItemRequest() {
        itemDto.setRequestId(10L);
        UserDTO user = userController.create(userDto);
        assertThrows(NotFoundException.class, () -> itemController.create(1L, itemDto));
    }

    @Test
    void updateTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        ItemDTO item = itemDto.toBuilder().name("new name").description("updateDescription").available(false).build();
        itemController.update(item, 1L, 1L);
        assertEquals(item.getDescription(), itemController.getById(1L, 1L).getDescription());
    }

    @Test
    void updateForWrongItemTest() {
        assertThrows(NotFoundException.class, () -> itemController.update(itemDto, 1L, 1L));
    }

    @Test
    void updateByWrongUserTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        assertThrows(NotFoundException.class, () -> itemController.update(itemDto.toBuilder()
                .name("new name").build(), 1L, 10L));
    }

    @Test
    void deleteTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        assertEquals(1, itemController.getAll(1L, 0, 10).size());
        itemController.delete(1L);
        assertEquals(0, itemController.getAll(1L, 0, 10).size());
    }

    @Test
    void searchTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        assertEquals(1, itemController.search("Desc", 0, 10).size());
    }

    @Test
    void searchEmptyTextTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        assertEquals(new ArrayList<ItemDTO>(), itemController.search("", 0, 10));
    }

    @Test
    void searchWithWrongFrom() {
        assertThrows(BadRequestException.class, () -> itemController.search("t", -1, 10));
    }

    @Test
    void createCommentTest() {
        UserDTO user = userController.create(userDto);
        ItemDTO item = itemController.create(1L, itemDto);
        UserDTO user2 = userController.create(userDto.toBuilder().email("email2@mail.com").build());
        bookingController.create(BookingShortDTO.builder()
                .start(LocalDateTime.of(2022, 10, 20, 12, 15))
                .end(LocalDateTime.of(2022, 10, 27, 12, 15))
                .itemId(item.getId()).build(), user2.getId());
        bookingController.approve(1L, 1L, true);
        itemController.createComment(item.getId(), user2.getId(), comment);
        assertEquals(1, itemController.getById(1L, 1L).getComments().size());
    }

    @Test
    void createCommentByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemController.createComment(1L, 1L, comment));
    }

    @Test
    void createCommentToWrongItem() {
        UserDTO user = userController.create(userDto);
        assertThrows(NotFoundException.class, () -> itemController.createComment(1L, 1L, comment));
        ItemDTO item = itemController.create(1L, itemDto);
        assertThrows(BadRequestException.class, () -> itemController.createComment(1L, 1L, comment));
    }

    @Test
    void getAllWithWrongFrom() {
        assertThrows(BadRequestException.class, () -> itemController.getAll(1L, -1, 10));
    }
}
