package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    private ItemRequest itemRequest;
    private User user;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(User.builder()
                .name("userName")
                .email("user@mail.ru")
                .build());
        itemRequest = itemRequestRepository.save(ItemRequest.builder()
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build());
    }

    @AfterEach
    void afterEach() {
        itemRequestRepository.deleteAll();
    }

    @Test
    void findById() {
        ItemRequest testItemRequest = itemRequestRepository.findById(1L).get();

        assertEquals(itemRequest.getId(), testItemRequest.getId());
        assertEquals(itemRequest.getDescription(), testItemRequest.getDescription());
    }
}