package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long userId = 0L;

    @Override
    public User addUser(User user) {
        checkEmail(user.getEmail());
        user.setId(++userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(UserDto userDto, long userId) {
        if (users.containsKey(userId)) {
            User user = users.get(userId);
            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                checkEmail(userDto.getEmail());
                user.setEmail(userDto.getEmail());
            }
            users.put(userId, user);
            return user;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteUser(long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            throw new NotFoundException();
        }
    }

    private void checkEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                throw new EmailAlreadyExistsException();
            }
        }
    }
}