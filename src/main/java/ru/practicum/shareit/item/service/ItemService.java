package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
public interface ItemService {
    List<Item> getAll();

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(Long itemId, ItemDto itemDto, Long userId);

    Item getById(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    List<ItemDto> searchItems(String text);
}
