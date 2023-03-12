package ru.practicum.shareit.bookingtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingShortDTO;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;
import static ru.practicum.shareit.booking.model.State.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTests {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    private ItemDTO itemDTO;

    private UserDTO userDTO;

    private UserDTO userDTO1;

    private BookingShortDTO bookingShortDTO;

    @BeforeEach
    void init() {
        itemDTO = ItemDTO.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        userDTO = UserDTO.builder()
                .name("name")
                .email("user@email.com")
                .build();

        userDTO1 = UserDTO.builder()
                .name("name")
                .email("user1@email.com")
                .build();

        bookingShortDTO = BookingShortDTO.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2023, 11, 10, 13, 0))
                .itemId(1L).build();
    }

    @Test
    void createTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDTO booking = bookingController.create(bookingShortDTO, user1.getId());
        assertEquals(1L, bookingController.getById(booking.getId(), user1.getId()).getId());
    }

    @Test
    void createByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> bookingController.create(bookingShortDTO, 1L));
    }

    @Test
    void createForWrongItemTest() {
        UserDTO user = userController.create(userDTO);
        assertThrows(NotFoundException.class, () -> bookingController.create(bookingShortDTO, 1L));
    }

    @Test
    void createByOwnerTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        assertThrows(NotFoundException.class, () -> bookingController.create(bookingShortDTO, 1L));
    }

    @Test
    void createToUnavailableItemTest() {
        UserDTO user = userController.create(userDTO);
        itemDTO.setAvailable(false);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        assertThrows(BadRequestException.class, () -> bookingController.create(bookingShortDTO, 2L));
    }

    @Test
    void createWithWrongEndDate() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        bookingShortDTO.setEnd(LocalDateTime.of(2022, 9, 24, 12, 30));
        assertThrows(BadRequestException.class, () -> bookingController.create(bookingShortDTO, user1.getId()));
    }

    @Test
    void approveTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDTO booking = bookingController.create(BookingShortDTO.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(WAITING, bookingController.getById(booking.getId(), user1.getId()).getStatus());
        bookingController.approve(booking.getId(), user.getId(), true);
        assertEquals(APPROVED, bookingController.getById(booking.getId(), user1.getId()).getStatus());
    }

    @Test
    void approveToWrongBookingTest() {
        assertThrows(NotFoundException.class, () -> bookingController.approve(1L, 1L, true));
    }

    @Test
    void approveByWrongUserTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDTO booking = bookingController.create(bookingShortDTO, user1.getId());
        assertThrows(NotFoundException.class, () -> bookingController.approve(1L, 2L, true));
    }

    @Test
    void approveToBookingWithWrongStatus() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDTO booking = bookingController.create(bookingShortDTO, user1.getId());
        bookingController.approve(1L, 1L, true);
        assertThrows(BadRequestException.class, () -> bookingController.approve(1L, 1L, true));
    }

    @Test
    void getAllByUserTest() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDTO booking = bookingController.create(bookingShortDTO, user1.getId());
        assertEquals(1, bookingController.getAllByUser(user1.getId(), State.WAITING, 0, 10).size());
        assertEquals(1, bookingController.getAllByUser(user1.getId(), ALL, 0, 10).size());
        assertEquals(0, bookingController.getAllByUser(user1.getId(), PAST, 0, 10).size());
        assertEquals(1, bookingController.getAllByUser(user1.getId(), CURRENT, 0, 10).size());
        assertEquals(0, bookingController.getAllByUser(user1.getId(), FUTURE, 0, 10).size());
        assertEquals(0, bookingController.getAllByUser(user1.getId(), REJECTED, 0, 10).size());
        bookingController.approve(booking.getId(), user.getId(), true);
        assertEquals(1, bookingController.getAllByOwner(user.getId(), CURRENT, 0, 10).size());
        assertEquals(1, bookingController.getAllByOwner(user.getId(), ALL, 0, 10).size());
        assertEquals(0, bookingController.getAllByOwner(user.getId(), State.WAITING, 0, 10).size());
        assertEquals(0, bookingController.getAllByOwner(user.getId(), FUTURE, 0, 10).size());
        assertEquals(0, bookingController.getAllByOwner(user.getId(), REJECTED, 0, 10).size());
        assertEquals(0, bookingController.getAllByOwner(user.getId(), PAST, 0, 10).size());
    }

    @Test
    void getAllByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> bookingController.getAllByUser(1L, ALL, 0, 10));
        assertThrows(NotFoundException.class, () -> bookingController.getAllByOwner(1L, ALL, 0, 10));
    }

    @Test
    void getByWrongIdTest() {
        assertThrows(NotFoundException.class, () -> bookingController.getById(1L, 1L));
    }

    @Test
    void getByWrongUser() {
        UserDTO user = userController.create(userDTO);
        ItemDTO item = itemController.create(user.getId(), itemDTO);
        UserDTO user1 = userController.create(userDTO1);
        BookingDTO booking = bookingController.create(bookingShortDTO, user1.getId());
        assertThrows(NotFoundException.class, () -> bookingController.getById(1L, 10L));
    }
}
