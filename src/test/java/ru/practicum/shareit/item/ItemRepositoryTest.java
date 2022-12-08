package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private Item item;


    @BeforeEach
    void beforeEach() {
        user = userRepository.save(User.builder()
                .id(1L)
                .name("Name")
                .email("name@email")
                .build());
        item = itemRepository.save(Item.builder()
                .id(1L)
                .name("Name")
                .description("Description")
                .available(true)
                .owner(user.getId())
                .request(null)
                .build());
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void search() {
        List<Item> list = itemRepository.search("name");
        assertEquals(list.get(0).getId(), item.getId());
        assertEquals(list.get(0).getName(), item.getName());
        assertEquals(list.get(0).getDescription(), item.getDescription());
    }

}