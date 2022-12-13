package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, long userId);

    Collection<UserDto> getUsers();

    UserDto getUserById(long userId);

    void deleteUser(long userId);
}