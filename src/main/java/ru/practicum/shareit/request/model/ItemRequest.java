package ru.practicum.shareit.request.model;


import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    @Length(max = 1024)
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id", referencedColumnName = "id")
    private User requestor;

    @Column(name = "created")
    private LocalDateTime created;

    public ItemRequest(String description, User requestor) {
        this.description = description;
        this.requestor = requestor;
        this.created = LocalDateTime.now();
    }
}