package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User addedUser = userStorage.addUser(user);
        return userMapper.toUserDto(addedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {
        User updatedUser = userStorage.updateUser(userDto, userId);
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public Collection<UserDto> getUsers() {
        Collection<UserDto> users = new ArrayList<>();
        for (User user : userStorage.getUsers()) {
            users.add(userMapper.toUserDto(user));
        }
        return users;
    }

    @Override
    public UserDto getUserById(long userId) {
        final Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }
        return userMapper.toUserDto(user.get());
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }
}