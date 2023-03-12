package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingShortDto;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.item.mapper.CommentMapper.toComment;
import static ru.practicum.shareit.item.mapper.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

/**
 * @project java-shareit
 * @auther George Prikashchenkov on 19.02.2023
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final ItemRequestRepository itemRequestRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDTO> getAll(Long userId, int from, int size) {
        List<ItemDTO> itemDTOList = itemRepository.findAllByOwnerId(userId, PageRequest.of(from, size))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        List<Long> itemIds = itemDTOList.stream().map(ItemDTO::getId).collect(Collectors.toList());

        List<Booking> lastBookings = bookingRepository.findAllByItemIdInAndStartBeforeOrderByItemIdAscStartAsc(itemIds, LocalDateTime.now());

        Map<Long, List<Booking>> itemToBookingsMap = lastBookings.stream().collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        itemDTOList.forEach(itemDto -> {
            List<Booking> bookings = itemToBookingsMap.get(itemDto.getId());
            if (bookings != null && !bookings.isEmpty()) {
                itemDto.setLastBooking(toBookingShortDto(bookings.get(0)));
                List<Booking> nextBookings = bookingRepository.findAllByItemIdAndStartAfterOrderByStartAsc(itemDto.getId(), bookings.get(0).getStart());
                if (!nextBookings.isEmpty()) {
                    itemDto.setNextBooking(toBookingShortDto(nextBookings.get(0)));
                }
            }

            itemDto.setComments(commentRepository.findAllByItemId(itemDto.getId()).stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        });

        return itemDTOList;
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDTO getById(Long id, Long ownerId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + id));
        ItemDTO itemDto = toItemDto(item);
        itemDto.setComments(commentRepository.findAllByItemId(id)
                .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));

        if (item.getOwner().getId().equals(ownerId)) {
            List<Booking> lastBookings = bookingRepository.findAllByItemIdAndStartBeforeOrderByStartAsc(id, LocalDateTime.now());
            if (!lastBookings.isEmpty()) {
                Booking lastBooking = bookingRepository.findAllByItemIdAndStartBeforeOrderByStartAsc(id, LocalDateTime.now()).get(0);
                itemDto.setLastBooking(toBookingShortDto(lastBooking));
            }

            if (itemDto.getLastBooking() != null) {
                List<Booking> nextBooking = bookingRepository.findAllByItemIdAndStartAfterOrderByStartAsc(itemDto.getId(), itemDto.getLastBooking().getStart());
                if (!nextBooking.isEmpty()) {
                    itemDto.setNextBooking(toBookingShortDto(nextBooking.get(0)));
                }
            }
        }

        return itemDto;
    }

    @Transactional
    @Override
    public ItemDTO create(ItemDTO itemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Невозможно создать вещь - " +
                        "не найден пользователь с id: " + userId));
        Item item = toItem(itemDto);
        item.setOwner(user);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Невозможно создать вещь - " +
                            "не найден запрос с id: " + itemDto.getRequestId()));
            item.setRequest(itemRequest);
        }
        itemRepository.save(item);

        return toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDTO update(ItemDTO itemDto, Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + id));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Невозможно обновить вещь - у пользователя с id: " + userId + "нет такой вещи");
        }
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);

        return toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDTO> search(String text, int from, int size) {
        List<ItemDTO> searchedItems = new ArrayList<>();
        if (text.isBlank()) {
            return searchedItems;
        }
        searchedItems = itemRepository.search(text, PageRequest.of(from, size))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        return searchedItems;
    }

    @Transactional
    @Override
    public CommentDTO createComment(Long itemId, Long userId, CommentDTO commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Невозможно создать комментарий - " +
                        "не существует пользователя с id " + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Невозможно создать комментарий - " +
                        "не существует вещи с id " + itemId));
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(userId, itemId, APPROVED,
                LocalDateTime.now()).isEmpty()) {
            throw new BadRequestException("Невозможно создать комментарий - " +
                    "вещь не бралась пользователем в аренду или аренда вещи еще не завершена");
        }
        Comment comment = toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        return toCommentDto(comment);
    }

}
