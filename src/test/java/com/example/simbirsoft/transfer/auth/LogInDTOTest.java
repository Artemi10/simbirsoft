package com.example.simbirsoft.transfer.auth;

import com.example.simbirsoft.exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogInDTOTest {

    @Test
    public void throw_Exception_When_Password_Is_Incorrect(){
        var incorrectDTO = new LogInDTO("lyah.artem10@mail.ru", "");
        var exception = assertThrows(AuthenticationException.class, incorrectDTO::check);
        assertEquals("Введён некорректный пароль", exception.getMessage());
    }

    @Test
    public void throw_Exception_When_Email_Is_Incorrect(){
        var incorrectDTO = new LogInDTO("lyah.artem10@mail", "qwerty");
        var exception = assertThrows(AuthenticationException.class, incorrectDTO::check);
        assertEquals("Введён некорректный email", exception.getMessage());
    }

    @Test
    public void execute_When_DTO_Is_Correct(){
        var incorrectDTO = new LogInDTO("lyah.artem10@mail.ru", "qwerty");
        assertDoesNotThrow(incorrectDTO::check);
    }
}
