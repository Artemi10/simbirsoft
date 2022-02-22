package com.example.simbirsoft.transfer.note;

import com.example.simbirsoft.exception.ValidatorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteDTOTest {

    @Test
    public void throw_Exception_When_Title_Is_Incorrect() {
        var incorrectDTO = new NoteDTO("", "Сходить в магазин");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Введён некорректный заголовок", exception.getMessage());
    }

    @Test
    public void throw_Exception_When_Text_Is_Incorrect() {
        var incorrectDTO = new NoteDTO("Покупки", "");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Введён некорректный текст", exception.getMessage());
    }

    @Test
    public void execute_When_DTO_Is_Correct(){
        var incorrectDTO = new NoteDTO("Покупки", "Сходить в магазин");
        assertDoesNotThrow(incorrectDTO::check);
    }
}
