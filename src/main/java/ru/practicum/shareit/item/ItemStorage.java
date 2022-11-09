package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item addItem(Item item);

    Item getItemById(long itemId);

    Item updateItem(ItemDto itemDto, long itemId);

    Collection<Item> getItemsByOwnerId(long ownerId);

    Collection<Item> searchItem(String text);
}