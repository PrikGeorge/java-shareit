package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    private final String header = "X-Sharer-User-Id";

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingShortDto bookingShortDto,
                             @RequestHeader(header) Long userId) {
        return bookingService.create(bookingShortDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable Long bookingId, @RequestHeader(header) Long userId,
                              @RequestParam Boolean approved) {
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(header) Long userId,
                                          @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllByOwner(userId, state);
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestHeader(header) Long userId,
                                         @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllByUser(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId, @RequestHeader(header) Long userId) {
        return bookingService.getById(bookingId, userId);
    }

}
