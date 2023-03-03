package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingShortDTO;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDTO create(BookingShortDTO bookingShortDto, Long userId);

    BookingDTO approve(Long bookingId, Long userId, Boolean approved);

    List<BookingDTO> getAllByOwner(Long userId, State state, int from, int size);

    List<BookingDTO> getAllByUser(Long userId, State state, int from, int size);

    BookingDTO getById(Long itemId, Long userId);
}
