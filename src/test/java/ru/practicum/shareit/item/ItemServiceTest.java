package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceTest {
    private ItemService itemService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;
    private User user;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository
        );
        user = new User(1, "UserName", "user@mail.ru");
        item = new Item(
                1,
                "itemName",
                "itemDescription",
                true,
                1
        );

        itemDto = ItemDto.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
        commentRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void addItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto testItem = itemService.addItem(itemDto, 1L);

        assertEquals(1L, testItem.getId());
        assertEquals(item.getName(), testItem.getName());
        assertEquals(item.getDescription(), testItem.getDescription());
        assertThrows(NotFoundException.class, () -> itemService.addItem(itemDto, 999L));
    }

    @Test
    void updateItem() {
        Item updatedItem = new Item(
                2,
                "Updated name",
                "Updated Description",
                true,
                1
        );

        ItemDto updatedDto = ItemDto.builder()
                .name("Updated name")
                .description("Updated Description")
                .available(true)
                .build();

        when(itemRepository.save(any())).thenReturn(updatedItem);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(updatedItem));
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        ItemDto testItem = itemService.updateItem(updatedDto, 2L, 1L);

        assertEquals(2L, testItem.getId());
        assertEquals(updatedItem.getName(), testItem.getName());
        assertEquals(updatedItem.getDescription(), testItem.getDescription());
        assertThrows(NotFoundException.class, () -> itemService.updateItem(updatedDto, 999L, 1L));
        assertThrows(AccessDeniedException.class, () -> itemService.updateItem(updatedDto, 2L, 9L));
    }

    @Test
    void getById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        ItemBookingDto testItem = itemService.getById(1L, 1L);

        assertEquals(1L, testItem.getId());
        assertEquals(item.getName(), testItem.getName());
        assertEquals(item.getDescription(), testItem.getDescription());
        assertEquals(0, testItem.getComments().size());
        assertNull(testItem.getLastBooking());
        assertThrows(NotFoundException.class, () -> itemService.getById(1L, 999L));
        assertThrows(NotFoundException.class, () -> itemService.getById(2L, 1L));

    }

    @Test
    void getItemsByOwnerId() {
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.findByItem_Id(1L)).thenReturn(List.of());

        List<ItemBookingDto> testItem = (List<ItemBookingDto>) itemService.getItemsByOwnerId(1L);
        assertEquals(1, testItem.size());
        assertEquals(1L, testItem.get(0).getId());
        assertEquals(item.getName(), testItem.get(0).getName());
        assertEquals(item.getDescription(), testItem.get(0).getDescription());
        assertEquals(0, testItem.get(0).getComments().size());
        assertEquals(List.of(), itemService.getItemsByOwnerId(999L));

    }

    @Test
    void searchItem() {
        when(itemRepository.search("itemName")).thenReturn(List.of(item));
        when(itemRepository.search("")).thenReturn(List.of());
        List<ItemDto> search = List.of(itemDto);
        List<ItemDto> testSearch = (List<ItemDto>) itemService.searchItem("itemName");
        List<ItemDto> emptyTestSearch = (List<ItemDto>) itemService.searchItem("");

        assertEquals(search.size(), testSearch.size());
        assertEquals(search.get(0).getDescription(), testSearch.get(0).getDescription());
        assertEquals(0, emptyTestSearch.size());
    }

    @Test
    void addComment() {
        CommentInDto commentInDto = new CommentInDto("comment text");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                Status.APPROVED,
                item,
                user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.findByBooker_Id(1L)).thenReturn(List.of(booking));
        when(commentRepository.save(any())).thenReturn(
                new Comment(1L, "comment text", 1L, 1L, "name", LocalDateTime.now()));

        CommentOutDto testCommentDto = itemService.addComment(1L, 1L, commentInDto);
        assertEquals(commentInDto.getText(), testCommentDto.getText());
    }
}