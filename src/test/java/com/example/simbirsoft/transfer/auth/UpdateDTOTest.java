package com.example.simbirsoft.transfer.auth;

import com.example.simbirsoft.exception.ValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UpdateDTOTest {
    @Test
    public void throw_Exception_When_Password_Is_Incorrect(){
        var incorrectDTO = new UpdateDTO("lyah.artem10@mail.ru", "",
                "", "d00a6c00-b554-4637-8939-77ad8e715244");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Введён некорректный пароль", exception.getMessage());
    }

    @Test
    public void throw_Exception_When_Email_Is_Incorrect(){
        var incorrectDTO = new UpdateDTO("lyah.artem10@mail", "qwerty",
                "qwerty", "d00a6c00-b554-4637-8939-77ad8e715244");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Введён некорректный email", exception.getMessage());
    }

    @Test
    public void throw_Exception_When_Repeated_Password_Is_Incorrect(){
        var incorrectDTO = new UpdateDTO("lyah.artem10@mail.ru", "qwerty",
                "qwerty1", "d00a6c00-b554-4637-8939-77ad8e715244");
        var exception = assertThrows(ValidatorException.class, incorrectDTO::check);
        assertEquals("Пароли не совпадают", exception.getMessage());
    }

    @Test
    public void throw_Exception_When_Token_Is_Incorrect(){
        var correctDTO = new UpdateDTO("lyah.artem10@mail.ru", "qwerty",
                "qwerty", " ");
        var exception = assertThrows(ValidatorException.class, correctDTO::check);
        assertEquals("Введён некорректный токен", exception.getMessage());
    }

    @Test
    public void execute_When_DTO_Is_Correct(){
        var incorrectDTO = new UpdateDTO("lyah.artem10@mail.ru", "qwerty",
                "qwerty", "d00a6c00-b554-4637-8939-77ad8e715244");
        assertDoesNotThrow(incorrectDTO::check);
    }
}
