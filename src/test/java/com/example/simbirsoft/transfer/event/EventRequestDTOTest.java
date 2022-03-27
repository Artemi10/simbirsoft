package com.example.simbirsoft.transfer.event;

import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.transfer.application.AppRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class EventRequestDTOTest {

    @Test
    public void throw_Exception_When_Name_Is_Incorrect() {
        var incorrectDTO = new EventRequestDTO(1, " ", "Description");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Введено некорректное название события", exception.getMessage());
    }

    @Test
    public void throw_Exception_When_Description_Is_Incorrect() {
        var incorrectDTO = new EventRequestDTO(1, "LogIn Event", " ");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Поле дополнительная информация не должно быть пустым", exception.getMessage());
    }

    @Test
    public void execute_When_DTO_Is_Correct(){
        var correctDTO = new EventRequestDTO(1, "LogIn Event", "Description");
        assertDoesNotThrow(correctDTO::check);
    }
}
