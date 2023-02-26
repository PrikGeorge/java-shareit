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

    Optional<Item> getById(Long id);

    Item create(Item item);

    Item update(Item item);

    void delete(Long id);
}
