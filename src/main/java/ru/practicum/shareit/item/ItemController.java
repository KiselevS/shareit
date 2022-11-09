package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
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

    @GetMapping
    public Collection<ItemDto> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getItemsByOwnerId(ownerId);
    }

    @GetMapping(path = "/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping(path = "/search")
    public Collection<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }
}