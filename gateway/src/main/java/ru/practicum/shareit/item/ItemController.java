package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                          @Validated({Create.class})
                                          @RequestBody ItemDto itemDto) {
        return itemClient.addItem(ownerId, itemDto);
    }

    @PatchMapping(path = "/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                             @RequestBody ItemDto itemDto,
                                             @PathVariable long itemId) {
        return itemClient.updateItem(ownerId, itemDto, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-id") long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentInDto commentInDto) {
        return itemClient.addComment(userId, itemId, commentInDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        return itemClient.getItemsByOwnerId(ownerId, from, size);
    }

    @GetMapping(path = "/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-id") long userId,
                                              @PathVariable long itemId) {
        return itemClient.getById(userId, itemId);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Positive @RequestParam(defaultValue = "10") int size) {
        return itemClient.searchItem(text, from, size);
    }
}