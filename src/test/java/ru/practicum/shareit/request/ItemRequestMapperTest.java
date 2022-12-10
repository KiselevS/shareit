package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ItemRequestMapperTest {

    @Test
    void toItemsRequest() {
        User owner = new User(1L, "Name", "e@mail.ru");
        User requestor = new User(2L, "Name", "re@mail.ru");
        ItemRequest request = new ItemRequest(1L, "Desc", requestor, LocalDateTime.now());
        Item item = new Item(1L, "Item", "Desc", true, owner.getId(), request);

        ItemRequestMapper mapper = new ItemRequestMapper();
        ItemRequestOutDto.Item itemRequestDto = mapper.toItemsRequest(item);
        assertEquals(itemRequestDto.getName(), item.getName());
        assertEquals(itemRequestDto.getDescription(), item.getDescription());
        assertEquals(itemRequestDto.isAvailable(), item.isAvailable());
    }
}