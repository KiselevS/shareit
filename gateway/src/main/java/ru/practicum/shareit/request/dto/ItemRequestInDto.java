package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestInDto {
    @NotNull
    private String description;
}
