package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentInDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           BookingRepository bookingRepository, CommentRepository commentRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, long ownerId) {
        Optional<User> owner = userRepository.findById(ownerId);

        if (owner.isPresent()) {
            Item item = ItemMapper.toItem(itemDto, owner.get().getId());
            if (itemDto.getRequestId() != null) {
                item.setRequest(itemRequestRepository
                        .findById(itemDto.getRequestId()).orElse(null));
            }
            Item addedItem = itemRepository.save(item);
            return ItemMapper.toItemDto(addedItem);
        }

        throw new NotFoundException("Владелец не найден");
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long ownerId) {

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (item.getOwner() != ownerId) {
            throw new AccessDeniedException("Только владелец может обновить информацию о вещи");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequestId() != null) {
            long requestId = itemDto.getRequestId();
            item.setRequest(itemRequestRepository.findById(requestId).get());
        }

        Item updatedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemBookingDto getById(Long userId, Long itemId) {

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        List<ItemBookingDto.Comment> comments = commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::toItemComment)
                .collect(Collectors.toList());

        ItemBookingDto itemBookingDto = ItemMapper.toItemBookingDto(item, null, null);

        if (item.getOwner() == userId) {
            List<Booking> bookings = bookingRepository.findByItem_IdAndItem_Owner(itemId, userId);


            if (!bookings.isEmpty()) {
                Booking nextBooking = bookings.stream().filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .min((Comparator.comparing(Booking::getStart))).orElse(null);
                Booking lastBooking = bookings.stream().filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .max((Comparator.comparing(Booking::getEnd))).orElse(null);
                ItemBookingDto.Booking next = null;
                ItemBookingDto.Booking last = null;
                if (nextBooking != null) {
                    next = new ItemBookingDto.Booking(nextBooking.getId(), nextBooking.getBooker().getId());
                }
                if (lastBooking != null) {
                    last = new ItemBookingDto.Booking(lastBooking.getId(), lastBooking.getBooker().getId());
                }

                itemBookingDto.setComments(comments);
                itemBookingDto.setLastBooking(last);
                itemBookingDto.setNextBooking(next);
                return itemBookingDto;
            }
        }

        itemBookingDto.setComments(comments);
        return itemBookingDto;
    }

    @Override
    public Collection<ItemBookingDto> getItemsByOwnerId(long ownerId, Pageable pageable) {
        List<ItemBookingDto> itemBookingDto = new ArrayList<>();
        List<Item> items = itemRepository.findAll(pageable).stream()
                .filter(item -> item.getOwner() == ownerId)
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());

        for (Item item : items) {

            List<Booking> bookings = bookingRepository.findByItem_Id(item.getId());
            if (!bookings.isEmpty()) {
                Booking nextBooking = bookings.stream().filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .min((Comparator.comparing(Booking::getStart))).orElse(null);
                Booking lastBooking = bookings.stream().filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .max((Comparator.comparing(Booking::getEnd))).orElse(null);
                ItemBookingDto.Booking next = null;
                ItemBookingDto.Booking last = null;
                if (nextBooking != null) {
                    next = new ItemBookingDto.Booking(nextBooking.getId(), nextBooking.getBooker().getId());
                }
                if (lastBooking != null) {
                    last = new ItemBookingDto.Booking(lastBooking.getId(), lastBooking.getBooker().getId());
                }
                itemBookingDto.add(ItemMapper.toItemBookingDto(item, last, next));
            } else itemBookingDto.add(ItemMapper.toItemBookingDto(item, null, null));
        }
        return itemBookingDto;
    }

    @Override
    public Collection<ItemDto> searchItem(String text, Pageable pageable) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository.search(text, pageable).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentOutDto addComment(Long userId, long itemId, CommentInDto commentInDto) {
        if (userId == null) {
            throw new BadRequestException("Заголовок X-Sharer-User-Id не найден");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (bookingRepository.findByBooker_Id(userId).stream()
                .anyMatch(booking -> booking.getItem().getId() == item.getId()
                        && !booking.getStatus().equals(Status.REJECTED)
                        && booking.getEnd().isBefore(LocalDateTime.now()))) {
            Comment comment = CommentMapper.toComment(commentInDto, item, user);
            comment = commentRepository.save(comment);
            return CommentMapper.toDto(comment);
        }
        throw new BadRequestException("Не удалось оставить комментарий");
    }
}