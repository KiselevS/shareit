package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-id";
    private final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private final LocalDateTime end = LocalDateTime.now().plusDays(2);

    private final BookingInDto bookingInDto = new BookingInDto(
            1L,
            start,
            end
    );
    private final BookingOutDto bookingOutDto = new BookingOutDto(
            1L,
            start,
            end,
            Status.APPROVED,
            new BookingOutDto.Booker(1L),
            new BookingOutDto.Item(1L, "item")
    );

    @Test
    void addBooking() throws Exception {
        when(bookingService.addBooking(bookingInDto, 1L)).thenReturn(bookingOutDto);

        mockMvc.perform(post("/bookings")
                        .header(USER_REQUEST_HEADER, 1L)
                        .content(mapper.writeValueAsString(bookingInDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.item.name", is("item")))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.status", is("APPROVED")));

        verify(bookingService, times(1)).addBooking(any(), anyLong());
    }

    @Test
    void getBookingsByState() throws Exception {
        when(bookingService.getBookingsByState(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingOutDto));

        mockMvc.perform(get("/bookings")
                        .header(USER_REQUEST_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", "0")
                        .param("size", "1")
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(bookingService, times(1))
                .getBookingsByState(anyLong(), anyString(), any(Pageable.class));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingOutDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(USER_REQUEST_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.item.name", is("item")))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.status", is("APPROVED")));

        verify(bookingService, times(1)).getBookingById(anyLong(), anyLong());

    }

    @Test
    void getBookingsByOwner() throws Exception {
        when(bookingService.getBookingsByOwner(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingOutDto));

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_REQUEST_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", "0")
                        .param("size", "1")
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(bookingService, times(1))
                .getBookingsByOwner(anyLong(), anyString(), any(Pageable.class));
    }

    @Test
    void setApprove() throws Exception {
        when(bookingService.setApprove(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingOutDto);

        mockMvc.perform(patch("/bookings/{bookingId}", "1")
                        .header(USER_REQUEST_HEADER, 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.item.name", is("item")))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.status", is("APPROVED")));

        verify(bookingService, times(1)).setApprove(anyLong(), anyLong(), anyBoolean());
    }
}