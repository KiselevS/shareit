package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentInDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private final ItemDto itemDto = new ItemDto(
            1L,
            "dtoName",
            "dtoDescription",
            true,
            1L);

    private final ItemBookingDto itemBookingDto = new ItemBookingDto(
            2L,
            "ItemBookingDto",
            "ItemBookingDto Description",
            true,
            new ItemBookingDto.Booking(1L, 1L),
            new ItemBookingDto.Booking(2L, 2L)
    );

    private static final String USER_REQUEST_HEADER = "X-Sharer-User-id";


    @Test
    void addItem() throws Exception {
        when(itemService.addItem(any(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("dtoName")))
                .andExpect(jsonPath("$.description", is("dtoDescription")));

        verify(itemService, Mockito.times(1)).addItem(any(), anyLong());
    }

    @Test
    void updateItem() throws Exception {
        ItemDto updatedItem = ItemDto.builder().description("Updated description").build();

        when(itemService.updateItem(any(), anyLong(), anyLong()))
                .thenReturn(updatedItem);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(updatedItem))
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(updatedItem.getDescription())));

        verify(itemService, Mockito.times(1)).updateItem(any(), anyLong(), anyLong());


    }

    @Test
    void addComment() throws Exception {
        CommentInDto commentInDto = new CommentInDto("Out Dto");

        CommentOutDto commentOutDto = new CommentOutDto(
                1L,
                "Out Dto",
                "Author",
                LocalDateTime.of(2022, 11, 21, 12, 25));

        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentOutDto);

        mockMvc.perform(post("/items/{itemId}/comment", "1")
                        .header(USER_REQUEST_HEADER, 1L)
                        .content(mapper.writeValueAsString(commentInDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.authorName", is(commentOutDto.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentOutDto.getText())));

        verify(itemService, Mockito.times(1)).addComment(anyLong(), anyLong(), any());
    }

    @Test
    void getItemsByOwnerId() throws Exception {
        when(itemService.getItemsByOwnerId(anyLong()))
                .thenReturn(List.of(itemBookingDto));

        mockMvc.perform(get("/items")
                        .param("text", anyString())
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is(itemBookingDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemBookingDto.getDescription())));

        verify(itemService, Mockito.times(1)).getItemsByOwnerId(anyLong());
    }

    @Test
    void getItemById() throws Exception {

        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemBookingDto);

        mockMvc.perform(get("/items/{itemId}", 2L)
                        .header(USER_REQUEST_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemBookingDto.getDescription())));

        verify(itemService, Mockito.times(1)).getById(anyLong(), anyLong());
    }

    @Test
    void searchItem() throws Exception {
        when(itemService.searchItem(anyString()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", anyString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));

        verify(itemService, Mockito.times(1)).searchItem(anyString());
    }
}