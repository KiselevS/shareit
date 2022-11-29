package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongStateException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    final BookingRepository bookingRepository;
    final UserRepository userRepository;
    final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingOutDto addBooking(BookingInDto bookingInDto, Long bookerId) {
        if (bookerId == null) {
            throw new BadRequestException("Заголовок X-Sharer-User-Id не найден");
        }

        Optional<Item> optionalItem = itemRepository.findById(bookingInDto.getItemId());
        if (optionalItem.isEmpty()) {
            throw new NotFoundException("Вещь не найдена");
        }

        Item item = optionalItem.get();

        if (item.getOwner() == bookerId) {
            throw new NotFoundException("Владелец не может бронировать собственные вещи");
        }

        Optional<User> optionalBooker = userRepository.findById(bookerId);
        if (optionalBooker.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        User booker = optionalBooker.get();

        if (!item.isAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }

        Booking booking = BookingMapper.toBooking(bookingInDto);
        booking.setBooker(booker);
        booking.setItem(item);

        booking = bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingOutDto setApprove(long bookingId, Long userId, boolean isApproved) {

        if (userId == null) {
            throw new BadRequestException("Заголовок X-Sharer-User-Id не найден");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException("Вещь не найдена");
        }

        Booking booking = optionalBooking.get();

        if (booking.getStatus() == Status.APPROVED) {
            throw new BadRequestException("Невозможно изменить статус");
        }

        Optional<User> optionalOwner = userRepository.findById(userId);
        if (optionalOwner.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (booking.getItem().getOwner() != userId) {
            throw new NotFoundException("Только владелец может изменить статус");
        }

        booking.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        booking = bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingOutDto getBookingById(long bookingId, Long userId) {
        if (userId == null) {
            throw new BadRequestException("Заголовок X-Sharer-User-Id не найден");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException("Бронирование не найдено");
        }
        Booking booking = optionalBooking.get();

        if (booking.getItem().getOwner() != userId
                && booking.getBooker().getId() != userId) {
            throw new NotFoundException("Только владелец и букер могут запросить бронирование");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingOutDto> getBookingsByState(Long userId, String state) {
        if (userId == null) {
            throw new BadRequestException("Заголовок X-Sharer-User-Id не найден");
        }
        if (Arrays.stream(State.values()).noneMatch(st -> st.toString().equals(state))) {
            throw new WrongStateException("Unknown state: " + state);
        }

        Optional<User> optionalBooker = userRepository.findById(userId);
        if (optionalBooker.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        List<Booking> bookings = new ArrayList<>();

        switch (State.valueOf(state)) {
            case ALL:
                bookings = bookingRepository.findByBooker_IdOrderByEndDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerCurrentBookings(userId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findByBookerPastBookings(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerFutureBookings(userId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByEndDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByEndDesc(userId, Status.REJECTED);
                break;
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingOutDto> getBookingsByOwner(Long ownerId, String state) {
        if (ownerId == null) {
            throw new BadRequestException("Заголовок X-Sharer-User-Id не найден");
        }

        if (Arrays.stream(State.values()).noneMatch(st -> st.toString().equals(state))) {
            throw new WrongStateException("Unknown state: " + state);
        }

        Optional<User> optionalOwner = userRepository.findById(ownerId);
        if (optionalOwner.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        List<Booking> bookings;

        switch (State.valueOf(state)) {
            case ALL:
                bookings = bookingRepository.findByItem_OwnerOrderByEndDesc(ownerId);
                break;
            case CURRENT:
                bookings = bookingRepository.findByOwnerIdCurrentBookings(ownerId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findByOwnerIdPastBookings(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findByOwnerIdFutureBookings(ownerId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findByItem_OwnerAndStatusOrderByEndDesc(ownerId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItem_OwnerAndStatusOrderByEndDesc(ownerId, Status.REJECTED);
                break;
            default:
                throw new WrongStateException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}