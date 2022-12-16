package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestOutDto addItemRequest(long userId, ItemRequestInDto itemRequestInDto);

    ItemRequestOutDto getItemRequestById(long userId, long requestId);

    List<ItemRequestOutDto> getItemRequestsByUserId(long userId);

    List<ItemRequestOutDto> getRequestsByOwnerWithPagination(long ownerId, Pageable pageable);
}
