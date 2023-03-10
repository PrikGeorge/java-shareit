package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String name;

    @NotBlank
    @Column(length = 1000, nullable = false)
    private String description;

    @Column(name = "is_available")
    @NotNull
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "id_owner", referencedColumnName = "id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ItemRequest request;
}
