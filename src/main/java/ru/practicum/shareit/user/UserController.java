package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto addUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PatchMapping(path = "/{userId}")
    public UserDto updateUser(@Validated(Update.class) @RequestBody UserDto userDto, @PathVariable long userId) {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
