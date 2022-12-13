package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentInDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long ownerId);

    ItemDto updateItem(ItemDto itemDto, long itemId, long ownerId);

    ItemBookingDto getById(Long userId, Long itemId);

    Collection<ItemBookingDto> getItemsByOwnerId(long ownerId, Pageable pageable);

    Collection<ItemDto> searchItem(String text, Pageable pageable);

    CommentOutDto addComment(Long userId, long itemId, CommentInDto commentInDto);
}