package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestOutDtoTest {

    @Autowired
    private JacksonTester<ItemRequestOutDto> json;

    @Test
    void testItemRequestDto() throws IOException {
        ItemRequestOutDto itemRequestOutDto = new ItemRequestOutDto(
                1,
                "description",
                2,
                LocalDateTime.of(2022, 12, 5, 10, 10),
                List.of(new ItemRequestOutDto.Item(
                        1, 1, "item", "item desc", true, 1))
        );

        JsonContent<ItemRequestOutDto> result = json.write(itemRequestOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-12-05T10:10:00");
        assertThat(result).extractingJsonPathArrayValue("$.items")
                .extracting("id")
                .contains(1);
        assertThat(result).extractingJsonPathArrayValue("$.items")
                .extracting("owner")
                .contains(1);
        assertThat(result).extractingJsonPathArrayValue("$.items")
                .extracting("name")
                .contains("item");
        assertThat(result).extractingJsonPathArrayValue("$.items")
                .extracting("description")
                .contains("item desc");
        assertThat(result).extractingJsonPathArrayValue("$.items")
                .extracting("available")
                .contains(true);
        assertThat(result).extractingJsonPathArrayValue("$.items")
                .extracting("requestId")
                .contains(1);
    }
}