package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-id";

    private final ItemRequestInDto itemRequestInDto = new ItemRequestInDto("description");
    private final ItemRequestOutDto itemRequestOutDto = new ItemRequestOutDto(
            1,
            "description",
            2,
            LocalDateTime.of(2022, 12, 5, 10, 10),
            List.of(new ItemRequestOutDto.Item(
                    1, 1, "item", "item desc", true, 1))
    );

    @Test
    void getItemRequestsByUserId() throws Exception {

        when(itemRequestService.getItemRequestsByUserId(anyLong()))
                .thenReturn(List.of(itemRequestOutDto));

        mockMvc.perform(get("/requests")
                        .header(USER_REQUEST_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("description")))
                .andExpect(jsonPath("$[0].requestorId", is(2)))
                .andExpect(jsonPath("$[0].created", is("2022-12-05T10:10:00")))
                .andExpect(jsonPath("$[0].items", hasSize(1)));

        verify(itemRequestService, times(1)).getItemRequestsByUserId(anyLong());
    }

    @Test
    void getItemRequests() throws Exception {
        when(itemRequestService.getRequestsByOwnerWithPagination(anyLong(), any()))
                .thenReturn(List.of(itemRequestOutDto));

        mockMvc.perform(get("/requests/all")
                        .header(USER_REQUEST_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("description")))
                .andExpect(jsonPath("$[0].requestorId", is(2)))
                .andExpect(jsonPath("$[0].created", is("2022-12-05T10:10:00")))
                .andExpect(jsonPath("$[0].items", hasSize(1)));

        verify(itemRequestService, times(1))
                .getRequestsByOwnerWithPagination(anyLong(), any());
    }

    @Test
    void getItemRequestById() throws Exception {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestOutDto);

        mockMvc.perform(get("/requests/{requestId}", "1")
                        .header(USER_REQUEST_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.requestorId", is(2)))
                .andExpect(jsonPath("$.created", is("2022-12-05T10:10:00")))
                .andExpect(jsonPath("$.items", hasSize(1)));

        verify(itemRequestService, times(1)).getItemRequestById(anyLong(), anyLong());
    }

    @Test
    void addItemRequest() throws Exception {
        when(itemRequestService.addItemRequest(anyLong(), any()))
                .thenReturn(itemRequestOutDto);

        mockMvc.perform(post("/requests")
                        .header(USER_REQUEST_HEADER, 1L)
                        .content(mapper.writeValueAsString(itemRequestInDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.requestorId", is(2)))
                .andExpect(jsonPath("$.created", is("2022-12-05T10:10:00")))
                .andExpect(jsonPath("$.items", hasSize(1)));

        verify(itemRequestService, times(1)).addItemRequest(anyLong(), any());
    }
}