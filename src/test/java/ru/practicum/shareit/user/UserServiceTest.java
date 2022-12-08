package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private User user1;
    private User user2;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        user1 = new User(1L, "user 1", "user1@email");
        user2 = new User(2L, "user 2", "user2@email");
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void addUser() {
        when(userRepository.save(any())).thenReturn(user1);

        UserDto userOutDto = userService.addUser(new UserDto(1, "user 1", "user1@email"));

        assertEquals(1L, userOutDto.getId());
        assertEquals("user 1", userOutDto.getName());
        assertEquals("user1@email", userOutDto.getEmail());
    }

    @Test
    void updateUser() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.save(any())).thenReturn(new User(2, "updatedName", "updated@mail.ru"));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserDto updatedUserDto = new UserDto(2L, "updatedName", "updated@mail.ru");
        UserDto userOutDto = userService.updateUser(updatedUserDto, 2L);

        assertEquals(2L, userOutDto.getId());
        assertEquals("updatedName", userOutDto.getName());
        assertEquals("updated@mail.ru", userOutDto.getEmail());
        assertThrows(NotFoundException.class, () -> userService.updateUser(updatedUserDto, 999L));
    }

    @Test
    void getUsers() {
        List<User> users = new ArrayList<>() {{
            add(user1);
            add(user2);
        }};
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> testUsers = (List<UserDto>) userService.getUsers();
        assertEquals(2, testUsers.size());
        assertEquals(1L, testUsers.get(0).getId());
        assertEquals("user 1", testUsers.get(0).getName());
        assertEquals("user2@email", testUsers.get(1).getEmail());
    }

    @Test
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserDto testUserDto = userService.getUserById(1);
        assertEquals(1L, testUserDto.getId());
        assertEquals("user 1", testUserDto.getName());
        assertEquals("user1@email", testUserDto.getEmail());

        assertThrows(NotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void deleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}