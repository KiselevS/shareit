package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestOutDto {
    private long id;
    private String description;
    private long requestorId;
    private LocalDateTime created;
    private List<Item> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private long id;
        private long owner;
        private String name;
        private String description;
        private boolean available;
        private long requestId;
    }
}
