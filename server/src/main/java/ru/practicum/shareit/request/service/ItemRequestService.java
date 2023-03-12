package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDTO;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDTO create(Long userId, ItemRequestDTO itemRequestDto);

    List<ItemRequestDTO> getAllByUser(Long userId);

    List<ItemRequestDTO> getAll(int from, int size, Long userId);

    ItemRequestDTO getById(Long requestId, Long userId);
}
