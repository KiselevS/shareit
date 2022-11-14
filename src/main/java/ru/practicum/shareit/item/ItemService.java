package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long ownerId);

    ItemDto updateItem(ItemDto itemDto, long itemId, long ownerId);

    ItemDto getItemById(long itemId);

    Collection<ItemDto> getItemsByOwnerId(long ownerId);

    Collection<ItemDto> searchItem(String text);
}