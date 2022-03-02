package com.example.simbirsoft.transfer.note;

import com.example.simbirsoft.exception.ValidatorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        var incorrectDTO = new RequestNoteDTO("Покупки", "Сходить в магазин");
        assertDoesNotThrow(incorrectDTO::check);
    }
}
