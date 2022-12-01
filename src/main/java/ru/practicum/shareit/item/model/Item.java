package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
