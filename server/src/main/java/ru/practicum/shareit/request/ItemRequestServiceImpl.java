package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  ItemRepository itemRepository,
                                  UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestOutDto addItemRequest(long userId, ItemRequestInDto itemRequestInDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestInDto, user);
        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper
                .toItemRequestOutDto(itemRequest);
    }

    @Override
    public ItemRequestOutDto getItemRequestById(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        List<ItemRequestOutDto.Item> items = itemRepository.findAllByRequest_Id(requestId).stream()
                .map(ItemRequestMapper::toItemsRequest)
                .collect(Collectors.toList());
        ItemRequestOutDto itemRequestOutDto = ItemRequestMapper.toItemRequestOutDto(itemRequest);
        itemRequestOutDto.setItems(items);
        return itemRequestOutDto;
    }

    @Override
    public List<ItemRequestOutDto> getItemRequestsByUserId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<ItemRequestOutDto> requests = itemRequestRepository.findAllByRequestor_Id(userId).stream()
                .map(ItemRequestMapper::toItemRequestOutDto)
                .collect(Collectors.toList());

        for (ItemRequestOutDto itemRequestDto : requests) {
            List<ItemRequestOutDto.Item> items = itemRepository.findAllByRequest_Id(itemRequestDto.getId()).stream()
                    .map(ItemRequestMapper::toItemsRequest)
                    .collect(Collectors.toList());
            itemRequestDto.setItems(items);
        }
        return requests;
    }

    @Override
    public List<ItemRequestOutDto> getRequestsByOwnerWithPagination(long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<ItemRequestOutDto> requests = itemRequestRepository.findAllByOwner_Id(userId, pageable).stream()
                .map(ItemRequestMapper::toItemRequestOutDto).collect(Collectors.toList());

        for (ItemRequestOutDto itemRequestDto : requests) {
            List<ItemRequestOutDto.Item> itemList = itemRepository.findAllByRequest_Id(itemRequestDto.getId()).stream()
                    .map(ItemRequestMapper::toItemsRequest)
                    .collect(Collectors.toList());
            itemRequestDto.setItems(itemList);
        }
        return requests;
    }
}
