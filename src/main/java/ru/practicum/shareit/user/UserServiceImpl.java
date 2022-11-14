package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User addedUser = userStorage.addUser(user);
        return UserMapper.toUserDto(addedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {
        User updatedUser = userStorage.updateUser(userDto, userId);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public Collection<UserDto> getUsers() {
        return userStorage.getUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        final Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }
}