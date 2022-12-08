package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.validation.DateConstraint;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@DateConstraint
@AllArgsConstructor
@Builder
public class BookingInDto {
    private Long itemId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}
