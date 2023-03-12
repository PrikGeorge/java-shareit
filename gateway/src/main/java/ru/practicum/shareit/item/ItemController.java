package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    private final String header = "X-Sharer-User-Id";

    @GetMapping
    public Object getItems(@RequestHeader(header) Long userId,
                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all items from user {}", userId);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public Object getItem(@PathVariable Long id,
                                          @RequestHeader(header) Long userId) {
        log.info("Get item {}", id);
        return itemClient.getItem(id, userId);
    }

    @PostMapping
    public Object createItem(@RequestHeader(header) Long userId,
                                             @RequestBody @Valid ItemRequestDto requestDto) {
        log.info("Create item");
        return itemClient.createItem(userId, requestDto);
    }

    @PatchMapping("/{id}")
    public Object updateItem(@RequestBody ItemRequestDto requestDto,
                                             @PathVariable Long id,
                                             @RequestHeader(header) Long userId) {
        log.info("Update item {}", id);
        return itemClient.updateItem(requestDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public Object deleteItem(@PathVariable Long id) {
        log.info("Delete item {}", id);
        return itemClient.deleteItem(id);
    }

    @GetMapping("/search")
    public Object searchItem(@RequestParam String text,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Search items by text {}", text);
        return itemClient.searchItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public Object createComment(@PathVariable Long itemId, @RequestHeader(header) Long userId,
                                                @Valid @RequestBody CommentRequestDto requestDto) {
        log.info("Create comment to item {}", itemId);
        return itemClient.createComment(itemId, userId, requestDto);
    }
}
