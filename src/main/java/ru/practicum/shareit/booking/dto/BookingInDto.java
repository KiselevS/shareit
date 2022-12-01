package ru.practicum.shareit.booking.dto;


import lombok.Data;
import ru.practicum.shareit.booking.validation.DateConstraint;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@DateConstraint
public class BookingInDto {
    private Long itemId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}
