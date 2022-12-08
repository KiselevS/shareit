package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemBookingDtoTest {

    @Autowired
    private JacksonTester<ItemBookingDto> json;

    @Test
    void itemBookingDtoTest() throws Exception {
        ItemBookingDto itemBookingDto = new ItemBookingDto(
                1,
                "name",
                "description",
                true,
                new ItemBookingDto.Booking(1L, 1L),
                new ItemBookingDto.Booking(2L, 2L),
                List.of()
        );

        JsonContent<ItemBookingDto> result = json.write(itemBookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.lastBooking")
                .extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.lastBooking")
                .extracting("bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.nextBooking")
                .extracting("id").isEqualTo(2);
        assertThat(result).extractingJsonPathValue("$.nextBooking")
                .extracting("bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathValue("comments").isEqualTo(List.of());

    }
}