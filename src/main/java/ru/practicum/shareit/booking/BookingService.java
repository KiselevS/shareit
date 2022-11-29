package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

public interface BookingService {

    BookingOutDto addBooking(BookingInDto bookingInDto, Long bookerId);

    BookingOutDto setApprove(long bookingId, Long userId, boolean isApproved);

    BookingOutDto getBookingById(long bookingId, Long userId);

    List<BookingOutDto> getBookingsByState(Long userId, String state);

    List<BookingOutDto> getBookingsByOwner(Long ownerId, String state);
}
