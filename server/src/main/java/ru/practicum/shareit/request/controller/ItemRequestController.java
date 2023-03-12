package ru.practicum.shareit.request.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    private final String header = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDTO create(@RequestHeader(header) Long userId,
                                 @RequestBody ItemRequestDTO itemRequestDto) {
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDTO> getAllByUser(@RequestHeader(header) Long userId) {
        return itemRequestService.getAllByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDTO> getAll(@RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestHeader(header) Long userId) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Невозможно найти запросы - " +
                    "неккоректно переданы параметры поиска - индекс первого элемента не может быть меньше нуля, " +
                    "а размер страницы должен быть больше нуля");
        }
        return itemRequestService.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDTO getById(@PathVariable Long requestId, @RequestHeader(header) Long userId) {
        return itemRequestService.getById(requestId, userId);
    }
}
