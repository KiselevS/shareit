package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
public class BookingOutDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private Booker booker;
    private Item item;

    public BookingOutDto(Long id, LocalDateTime start, LocalDateTime end, Status status, Booker booker, Item item) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.booker = booker;
        this.item = item;
    }

    @Data
    public static class Booker {
        private Long id;

        public Booker(long id) {
            this.id = id;
        }
    }

    @Data
    public static class Item {
        private Long id;
        private String name;

        public Item(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}