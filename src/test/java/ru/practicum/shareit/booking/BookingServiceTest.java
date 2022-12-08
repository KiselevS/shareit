package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {
    private BookingService bookingService;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private User user;
    private User booker;
    private Item item;
    private Booking booking;
    private LocalDateTime start = LocalDateTime.of(2023, 12, 10, 10, 10);
    private LocalDateTime end = LocalDateTime.of(2023, 12, 12, 10, 10);

    private BookingInDto bookingInDto;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        user = new User(1, "UserName", "user@mail.ru");
        booker = new User(2, "BookerName", "Booker@mail.ru");

        item = new Item(
                1,
                "itemName",
                "itemDescription",
                true,
                1
        );

        booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                Status.WAITING,
                item,
                booker);

        bookingInDto = BookingInDto.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void addBooking() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingOutDto testBooking = bookingService.addBooking(bookingInDto, 2L);

        assertEquals(1L, testBooking.getId());
        assertEquals(booking.getStatus(), testBooking.getStatus());
        assertEquals(booking.getItem().getId(), testBooking.getItem().getId());
        assertEquals(booking.getStart(), testBooking.getStart());
        assertEquals(booking.getEnd(), testBooking.getEnd());
    }

    @Test
    void setApprove() {
        Booking approvedBooking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                Status.APPROVED,
                item,
                booker);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(approvedBooking);

        BookingOutDto testBooking = bookingService.setApprove(1L, 1L, true);

        assertEquals(1L, testBooking.getId());
        assertEquals(approvedBooking.getStatus(), testBooking.getStatus());
    }

    @Test
    void getBookingById() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingOutDto testBooking = bookingService.getBookingById(1L, 1L);

        assertEquals(1L, testBooking.getId());
        assertEquals(booking.getStatus(), testBooking.getStatus());
        assertEquals(booking.getItem().getId(), testBooking.getItem().getId());
        assertEquals(booking.getStart(), testBooking.getStart());
        assertEquals(booking.getEnd(), testBooking.getEnd());
    }

    @Test
    void getBookingsByState() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker_IdOrderByEndDesc(any(), any())).thenReturn(List.of(booking));

        List<BookingOutDto> testBookings = bookingService.getBookingsByState(1L, "ALL", Pageable.unpaged());

        assertEquals(1, testBookings.size());
        assertEquals(1L, testBookings.get(0).getId());
    }

    @Test
    void getBookingsByOwner() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByItem_OwnerOrderByEndDesc(2L, Pageable.unpaged())).thenReturn(List.of(booking));

        List<BookingOutDto> testBookings = bookingService.getBookingsByOwner(2L, "ALL", Pageable.unpaged());

        assertEquals(1, testBookings.size());
        assertEquals(1L, testBookings.get(0).getId());
    }
}