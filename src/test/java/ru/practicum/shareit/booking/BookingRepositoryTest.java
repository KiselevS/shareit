package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(User.builder()
                .name("userName")
                .email("user@mail.ru")
                .build());
        item = itemRepository.save(Item.builder()
                .name("Name")
                .description("Description")
                .available(true)
                .owner(user.getId())
                .request(null)
                .build());
        booking = bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Status.WAITING)
                .item(item)
                .booker(user).build());
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void findById() {
        Booking tetsBooking = bookingRepository.findById(1L).get();

        assertEquals(1, tetsBooking.getId());
        assertEquals(booking.getStatus(), tetsBooking.getStatus());
        assertEquals(booking.getStart(), tetsBooking.getStart());
        assertEquals(booking.getEnd(), tetsBooking.getEnd());
    }

}