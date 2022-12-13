package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.validation.DateConstraint;

import java.time.LocalDateTime;

@Data
@DateConstraint
@AllArgsConstructor
@Builder
public class BookingInDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
