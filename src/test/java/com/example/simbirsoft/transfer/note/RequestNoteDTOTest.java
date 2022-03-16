package com.example.simbirsoft.transfer.note;

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
class RequestNoteDTOTest {

    @Test
    public void throw_Exception_When_Title_Is_Incorrect() {
        var incorrectDTO = new RequestNoteDTO("", "Сходить в магазин");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Введён некорректный заголовок", exception.getMessage());
    }

    @Test
    public void throw_Exception_When_Text_Is_Incorrect() {
        var incorrectDTO = new RequestNoteDTO("Покупки", "");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Введён некорректный текст", exception.getMessage());
    }

    @Test
    public void execute_When_DTO_Is_Correct(){
        var correctDTO = new RequestNoteDTO("Покупки", "Сходить в магазин");
        assertDoesNotThrow(correctDTO::check);
    }
}
