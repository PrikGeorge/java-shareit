package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
public interface ItemRepository {
    List<Item> getAll();
    Item save(Item item);
    Item findById(Long itemId);
    List<Item> findByOwner(Long ownerId);
    List<Item> searchItems(String text);
}
