package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.dto.CommentInDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long ownerId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping(path = "/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestBody ItemDto itemDto, @PathVariable long itemId) {
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutDto addComment(@RequestHeader("X-Sharer-User-id") long userId,
                                    @PathVariable long itemId,
                                    @Validated @RequestBody CommentInDto commentInDto) {
        return itemService.addComment(userId, itemId, commentInDto);
    }

    @GetMapping
    public Collection<ItemBookingDto> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getItemsByOwnerId(ownerId);
    }

    @GetMapping(path = "/{itemId}")
    public ItemBookingDto getItemById(@RequestHeader("X-Sharer-User-id") long userId,
                                      @PathVariable long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping(path = "/search")
    public Collection<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }
}