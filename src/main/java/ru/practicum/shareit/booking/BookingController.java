package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingOutDto addBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                    @Validated @RequestBody BookingInDto bookingInDto) {
        return bookingService.addBooking(bookingInDto, bookerId);
    }

    @GetMapping
    public List<BookingOutDto> getBookingsByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL")
                                                  String state) {
        return bookingService.getBookingsByState(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getBookingById(@PathVariable long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                  @RequestParam(required = false, defaultValue = "ALL")
                                                  String state) {
        return bookingService.getBookingsByOwner(ownerId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto setApprove(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.setApprove(bookingId, userId, approved);
    }
}