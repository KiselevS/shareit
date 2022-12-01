package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentOutDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
