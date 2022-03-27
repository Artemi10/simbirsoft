package com.example.simbirsoft.transfer.application;

import com.example.simbirsoft.exception.ValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AppRequestDTOTest {

    @Test
    public void throw_Exception_When_Name_Is_Incorrect() {
        var incorrectDTO = new AppRequestDTO("");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Введено некорректное название приложения", exception.getMessage());
    }

    @Test
    public void execute_When_DTO_Is_Correct(){
        var correctDTO = new AppRequestDTO("Simple CRUD App");
        assertDoesNotThrow(correctDTO::check);
    }
}
