package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDTO;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class ItemDTO {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

    private BookingShortDTO lastBooking;

    private BookingShortDTO nextBooking;

    private List<CommentDTO> comments;
}
