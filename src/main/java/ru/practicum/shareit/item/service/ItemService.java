package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
public interface ItemService {
    List<ItemDto> getAll(Long userId);

    ItemDto getById(Long id);

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long id, Long userId);

    void delete(Long id);

    List<ItemDto> search(String text);
}
