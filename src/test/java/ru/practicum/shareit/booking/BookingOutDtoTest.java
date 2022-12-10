package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingOutDtoTest {

    @Autowired
    private JacksonTester<BookingOutDto> json;

    @Test
    void testBookingDto() throws IOException {
        final LocalDateTime start = LocalDateTime.now().plusDays(1);
        final LocalDateTime end = LocalDateTime.now().plusDays(2);

        final BookingOutDto bookingOutDto = new BookingOutDto(
                1L,
                start,
                end,
                Status.APPROVED,
                new BookingOutDto.Booker(1L),
                new BookingOutDto.Item(1L, "item")
        );

        JsonContent<BookingOutDto> result = json.write(bookingOutDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}