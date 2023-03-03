package ru.practicum.shareit.usertests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTests {
    @Autowired
    JacksonTester<UserDTO> json;

    @Test
    void testUserDto() throws Exception {
        UserDTO userDto = UserDTO
                .builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();

        JsonContent<UserDTO> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("user");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("user@mail.ru");
    }
}
