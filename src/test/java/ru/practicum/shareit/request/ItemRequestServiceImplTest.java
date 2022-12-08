package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplTest {
    private ItemRequestService itemRequestService;
    private ItemRequestRepository itemRequestRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private User user;
    private User requestor;
    private Item item;
    private ItemRequest itemRequest;
    private ItemRequestInDto itemRequestInDto;
    private ItemRequestOutDto itemRequestOutDto;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, itemRepository, userRepository);

        user = new User(1, "UserName", "user@mail.ru");
        requestor = new User(2, "Requestor", "req@mail.ru");
        item = new Item(
                1,
                "itemName",
                "itemDescription",
                true,
                1
        );
        itemRequest = new ItemRequest(
                1,
                "request",
                requestor,
                LocalDateTime.of(2022, 12, 5, 10, 10)
        );
        itemRequestInDto = ItemRequestInDto.builder()
                .description("request").build();
        itemRequestOutDto = new ItemRequestOutDto(
                1,
                "request",
                2,
                LocalDateTime.of(2022, 12, 5, 10, 10),
                List.of(new ItemRequestOutDto.Item(
                        1, 1, "itemName", "itemDescription", true, 1))
        );
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void addItemRequest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequestOutDto testRequest = itemRequestService.addItemRequest(2, itemRequestInDto);
        assertEquals(1L, testRequest.getId());
        assertEquals(itemRequestOutDto.getDescription(), testRequest.getDescription());
        assertEquals(itemRequestOutDto.getRequestorId(), testRequest.getRequestorId());
    }

    @Test
    void getItemRequestById() {
        when(userRepository.findById(any())).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findById(any())).thenReturn(Optional.of(itemRequest));

        ItemRequestOutDto testRequest = itemRequestService.getItemRequestById(2, 1);
        assertEquals(1L, testRequest.getId());
        assertEquals(itemRequestOutDto.getDescription(), testRequest.getDescription());
        assertEquals(itemRequestOutDto.getRequestorId(), testRequest.getRequestorId());
    }

    @Test
    void getItemRequestsByUserId() {
        when(userRepository.findById(any())).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findAllByRequestor_Id(2L)).thenReturn(List.of(itemRequest));

        List<ItemRequestOutDto> testRequest = itemRequestService.getItemRequestsByUserId(2L);
        assertEquals(1, testRequest.size());
        assertEquals(itemRequestOutDto.getId(), testRequest.get(0).getId());
    }

    @Test
    void getRequestsByOwnerWithPagination() {
        when(userRepository.findById(any())).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findAllByOwner_Id(1L, Pageable.unpaged())).thenReturn(List.of(itemRequest));
        List<ItemRequestOutDto> testRequest = itemRequestService.getRequestsByOwnerWithPagination(1L, Pageable.unpaged());
        assertEquals(1, testRequest.size());
        assertEquals(itemRequestOutDto.getId(), testRequest.get(0).getId());
    }
}