package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(max = 32)
    @Column(name = "name", nullable = false)
    private String name;

    @Length(max = 1024)
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_available")
    private boolean available;

    @Column(name = "owner_id")
    private long owner;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ItemRequest request;

    public Item(long id, String name, String description, Boolean available, long ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = ownerId;
    }
}
