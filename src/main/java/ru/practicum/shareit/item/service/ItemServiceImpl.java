package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.IdentityGenerator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.getAll();
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toEntity(itemDto);
        item.setOwner(userId);

        if (Objects.isNull(item.getId())) {
            item.setId(IdentityGenerator.INSTANCE.generateId(Item.class));
        }

        if (existsByNameAndOwner(item.getName(), item.getOwner())) {
            throw new DuplicateEmailException("Item with name " + item.getName() + " already exists for owner " + item.getOwner());
        }

        Item savedItem = itemRepository.save(item);
        return ItemMapper.toDTO(savedItem);
    }

    @Override
    public ItemDto update(Long itemId, ItemDto itemDto, Long userId) {
        Item existingItem = getById(itemId);

        if (!existingItem.getOwner().equals(userId)) {
            throw new AccessDeniedException("You are not the owner of this item");
        }

        existingItem.setName(Objects.requireNonNullElse(itemDto.getName(), existingItem.getName()));
        existingItem.setDescription(Objects.requireNonNullElse(itemDto.getDescription(), existingItem.getDescription()));
        existingItem.setAvailable(Objects.requireNonNullElse(itemDto.getAvailable(), existingItem.getAvailable()));

        return ItemMapper.toDTO(itemRepository.save(existingItem));
    }

    @Override
    public Item getById(Long id) {
        Item item = itemRepository.findById(id);
        if (Objects.isNull(item)) {
            log.error("Item with id " + id + " not found");
            throw new NotFoundException("Item with id " + id + " not found");
        }

        return item;
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        List<Item> items = itemRepository.findByOwner(userId);
        return items.stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> items = itemRepository.searchItems(text);
        return items.stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    private boolean existsByNameAndOwner(String name, Long ownerId) {
        return getAll().stream()
                .anyMatch(item -> item.getName().equals(name) && item.getOwner().equals(ownerId));
    }
}
