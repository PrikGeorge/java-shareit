package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Long, Item> itemMap = new HashMap<>();

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(itemMap.values());
    }

    @Override
    public Item save(Item item) {
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findById(Long itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> findByOwner(Long ownerId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemMap.values().stream()
                .filter(item -> item.getAvailable() && (item.getName().contains(text) || item.getDescription().contains(text)))
                .collect(Collectors.toList());
    }

}
