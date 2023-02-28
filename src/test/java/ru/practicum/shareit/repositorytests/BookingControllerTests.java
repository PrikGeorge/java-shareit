package ru.practicum.shareit.repositorytests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTests {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    private ItemDTO itemDTO = ItemDTO.builder()
            .name("name")
            .description("description")
            .available(true)
            .build();

    private UserDTO userDTO = UserDTO.builder()
            .name("name")
            .email("user@email.com")
            .build();

    private UserDTO userDTO1 = UserDTO.builder()
            .name("name")
            .email("user1@email.com")
            .build();

    @Test
    void createTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDto booking = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(1L, bookingController.getById(booking.getId(), user1.getId()).getId());
    }

    @Test
    void approveTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDto booking = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(WAITING, bookingController.getById(booking.getId(), user1.getId()).getStatus());
        bookingController.approve(booking.getId(), user.getId(), true);
        assertEquals(APPROVED, bookingController.getById(booking.getId(), user1.getId()).getStatus());
    }

    @Test
    void getAllByUserTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDto booking = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(1, bookingController.getAllByUser(user1.getId(), "WAITING").size());
        bookingController.approve(booking.getId(), user.getId(), true);
        assertEquals(1, bookingController.getAllByOwner(user.getId(), "CURRENT").size());
    }
}
