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
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long ownerId) {
        if (userStorage.getUserById(ownerId).isEmpty()) {
            throw new NotFoundException("Вещь не найдена");
        }
        Item item = ItemMapper.toItem(itemDto, ownerId);
        Item addedItem = itemStorage.addItem(item);
        return ItemMapper.toItemDto(addedItem);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long ownerId) {
        if (itemStorage.getItemById(itemId).getOwner() != ownerId) {
            throw new AccessDeniedException("Только владелец может обновить информацию о вещи");
        }
        Item updatedItem = itemStorage.updateItem(itemDto, itemId);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return ItemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public Collection<ItemDto> getItemsByOwnerId(long ownerId) {
        return itemStorage.getItemsByOwnerId(ownerId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemStorage.searchItem(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}