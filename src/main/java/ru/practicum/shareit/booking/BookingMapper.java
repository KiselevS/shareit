package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapper {
    public static Booking toBooking(BookingInDto bookingInDto) {
        return new Booking(bookingInDto.getStart(), bookingInDto.getEnd());
    }

    public static BookingOutDto toBookingDto(Booking booking) {
        return new BookingOutDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new BookingOutDto.Booker(booking.getBooker().getId()),
                new BookingOutDto.Item(booking.getItem().getId(), booking.getItem().getName()));
    }
}
