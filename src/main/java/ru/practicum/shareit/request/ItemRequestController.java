package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @GetMapping
    public List<ItemRequestOutDto> getItemRequestsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        int page = from / size;
        PageRequest request = PageRequest.of(page, size);
        return itemRequestService.getRequestsByOwnerWithPagination(userId, request);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long requestId) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }

    @PostMapping
    public ItemRequestOutDto addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody @Validated ItemRequestInDto itemRequestInDto) {
        return itemRequestService.addItemRequest(userId, itemRequestInDto);
    }
}
