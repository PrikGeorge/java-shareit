package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    private final String header = "X-Sharer-User-Id";

    @PostMapping
    public Object createItemRequest(@RequestHeader(header) Long userId,
                                                    @Valid @RequestBody ItemRequestRequestDto requestDto) {
        log.info("Create item request by user {}", userId);
        return itemRequestClient.createItemRequest(userId, requestDto);
    }

    @GetMapping
    public Object getItemRequestsByUser(@RequestHeader(header) Long userId) {
        log.info("Get all user {} item requests", userId);
        return itemRequestClient.getItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public Object getAllItemRequests(@RequestHeader(header) long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all item requests without user {}", userId);
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public Object getItemRequest(@PathVariable Long requestId,
                                                 @RequestHeader(header) Long userId) {
        log.info("Get item request {}", requestId);
        return itemRequestClient.getItemRequest(requestId, userId);
    }
}
