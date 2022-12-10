package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserInDto;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private final UserDto userDto1 = new UserDto(1L, "UserDtoName1", "UserDto1@mail.ru");

    @Test
    void getUsers() throws Exception {
        when(userService.getUsers())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService, times(1)).getUsers();
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(1))
                .thenReturn(userDto1);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void addUser() throws Exception {
        when(userService.addUser(any()))
                .thenReturn(userDto1);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));

        verify(userService, times(1)).addUser(any());
    }

    @Test
    void updateUser() throws Exception {
        UserInDto userInDto = new UserInDto("updatedName", "updated@mail.ru");
        UserDto updatedUserDto = new UserDto(3, "updatedName", "updated@mail.ru");

        when(userService.updateUser(any(), anyLong()))
                .thenReturn(updatedUserDto);

        mockMvc.perform(patch("/users/{id}", 3L)
                        .content(mapper.writeValueAsString(userInDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is(userInDto.getName())))
                .andExpect(jsonPath("$.email", is(userInDto.getEmail())));

        verify(userService, times(1)).updateUser(any(), anyLong());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(anyLong());
    }
}