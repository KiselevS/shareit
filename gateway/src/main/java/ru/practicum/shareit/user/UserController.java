package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return userClient.addUser(userDto);
    }

    @PatchMapping(path = "/{userId}")
    public ResponseEntity<Object> updateUser(@Validated(Update.class) @RequestBody UserDto userDto,
                                             @PathVariable long userId) {
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        return userClient.deleteUser(userId);
    }
}
