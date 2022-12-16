package ru.practicum.shareit.request;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

public class ItemRequestMapper {
    public static ItemRequestOutDto toItemRequestOutDto(ItemRequest itemRequest) {
        return new ItemRequestOutDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor().getId(),
                itemRequest.getCreated(),
                new ArrayList<>());
    }

    public static ItemRequest toItemRequest(ItemRequestInDto itemRequestInDto, User requester) {
        return new ItemRequest(itemRequestInDto.getDescription(), requester);
    }

    public static ItemRequestOutDto.Item toItemsRequest(Item item) {
        return new ItemRequestOutDto.Item(item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest().getId());
    }
}
