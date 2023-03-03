package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.List;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
public interface ItemService {
    List<ItemDTO> getAll(Long userId);

    ItemDTO getById(Long id, Long ownerId);

    ItemDTO create(ItemDTO itemDto, Long userId);

    ItemDTO update(ItemDTO itemDto, Long id, Long userId);

    void delete(Long id);

    List<ItemDTO> search(String text);

    CommentDTO createComment(Long itemId, Long userId, CommentDTO commentDto);
}
