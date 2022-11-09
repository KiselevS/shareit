package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private long itemId = 0L;

    @Override
    public Item addItem(Item item) {
        item.setId(++itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Item updateItem(ItemDto itemDto, long itemId) {
        if (items.containsKey(itemId)) {
            Item item = items.get(itemId);
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            items.put(itemId, item);
            return item;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Collection<Item> getItemsByOwnerId(long ownerId) {
        Collection<Item> itemsByOwnerId = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner() == ownerId) {
                itemsByOwnerId.add(item);
            }
        }
        return itemsByOwnerId;
    }

    @Override
    public Collection<Item> searchItem(String text) {
        Collection<Item> searchedItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.isAvailable() && (item.getName().toLowerCase().contains(text.toLowerCase())
                                    || item.getDescription().toLowerCase().contains(text.toLowerCase()))) {
                searchedItems.add(item);
            }
        }
        return searchedItems;
    }
}