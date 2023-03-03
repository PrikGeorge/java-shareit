package ru.practicum.shareit.item.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private final String header = "X-Sharer-User-Id";

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDTO> getAll(@RequestHeader(header) Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDTO getById(@PathVariable Long id, @RequestHeader(header) Long userId) {
        return itemService.getById(id, userId);
    }

    @PostMapping
    public ItemDTO create(@RequestHeader(header) Long userId, @Valid @RequestBody ItemDTO itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDTO update(@RequestBody ItemDTO itemDto, @PathVariable Long id,
                          @RequestHeader(header) Long userId) {
        return itemService.update(itemDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDTO> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDTO createComment(@PathVariable Long itemId, @RequestHeader(header) Long userId,
                                    @Valid @RequestBody CommentDTO commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
