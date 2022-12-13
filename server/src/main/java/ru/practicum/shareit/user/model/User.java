package ru.practicum.shareit.user.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @Length(max = 32)
    @Column(name = "name", nullable = false)
    private String name;

    @Length(max = 32)
    @Column(name = "email", unique = true, nullable = false)
    private String email;
}