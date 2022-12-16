package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

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
                                                  @RequestParam(defaultValue = "0")
                                                  int from,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(required = false, defaultValue = "ALL")
                                                  String state) {
        int page = from / size;
        return bookingService.getBookingsByState(userId, state, PageRequest.of(page, size));
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getBookingById(@PathVariable long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                  @RequestParam(defaultValue = "0")
                                                  int from,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(required = false, defaultValue = "ALL")
                                                  String state) {
        int page = from / size;
        return bookingService.getBookingsByOwner(ownerId, state, PageRequest.of(page, size));
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto setApprove(@PathVariable long bookingId,
                                    @RequestParam boolean approved,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.setApprove(bookingId, userId, approved);
    }
}