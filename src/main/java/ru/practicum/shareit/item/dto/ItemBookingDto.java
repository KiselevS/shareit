package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemBookingDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    Booking lastBooking;
    Booking nextBooking;
    List<Comment> comments;

    public ItemBookingDto(long id, String name, String description, boolean available, Booking lastBooking, Booking nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    public static class Booking {
        Long id;
        Long bookerId;
    }

    @Data
    @AllArgsConstructor
    public static class Comment {
        Long id;
        String text;
        String authorName;
        LocalDateTime created;
    }
}
