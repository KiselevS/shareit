package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    User updateUser(UserDto userDto, long userId);

    Collection<User> getUsers();

    Optional<User> getUserById(long userId);

    void deleteUser(long userId);
}