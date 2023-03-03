package ru.practicum.shareit.itemrequesttests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDTO;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDTOJsonTests {
    @Autowired
    JacksonTester<ItemRequestDTO> json;

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDTO itemRequestDto = ItemRequestDTO
                .builder()
                .id(1L)
                .description("descriptionOfItemRequest")
                .build();

        JsonContent<ItemRequestDTO> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("descriptionOfItemRequest");
    }
}
