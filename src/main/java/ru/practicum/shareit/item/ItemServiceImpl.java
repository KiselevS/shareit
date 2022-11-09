package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long ownerId) {
        if (userStorage.getUserById(ownerId).isEmpty()) {
            throw new NotFoundException();
        }
        Item item = itemMapper.toItem(itemDto, ownerId);
        Item addedItem = itemStorage.addItem(item);
        return itemMapper.toItemDto(addedItem);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long ownerId) {
        if (itemStorage.getItemById(itemId).getOwner() != ownerId) {
            throw new AccessDeniedException();
        }
        Item updatedItem = itemStorage.updateItem(itemDto, itemId);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public Collection<ItemDto> getItemsByOwnerId(long ownerId) {
        Collection<ItemDto> items = new ArrayList<>();
        for (Item item : itemStorage.getItemsByOwnerId(ownerId)) {
            items.add(itemMapper.toItemDto(item));
        }
        return items;
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        Collection<ItemDto> searchedItems = new ArrayList<>();
        if (text.isBlank()) {
            return searchedItems;
        }
        for (Item item : itemStorage.searchItem(text)) {
            searchedItems.add(itemMapper.toItemDto(item));
        }
        return searchedItems;
    }
}