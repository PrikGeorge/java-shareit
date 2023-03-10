package ru.practicum.shareit.itemtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDTO;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDTOJsonTests {
    @Autowired
    private JacksonTester<ItemDTO> json;

    @Test
    void testItemDto() throws Exception {
        ItemDTO itemDto = ItemDTO
                .builder()
                .id(1L)
                .name("item")
                .available(true)
                .description("descriptionOfItem")
                .build();

        JsonContent<ItemDTO> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("descriptionOfItem");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }
}
