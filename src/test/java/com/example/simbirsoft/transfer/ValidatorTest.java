package com.example.simbirsoft.transfer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.example.simbirsoft.transfer.Validator.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ValidatorTest {

    @Test
    public void invalidTextFieldTest() {
        assertFalse(isTextFieldValid(null));
        assertFalse(isTextFieldValid(""));
        assertFalse(isTextFieldValid("   "));
    }

    @Test
    public void validTextFieldTest() {
        assertTrue(isTextFieldValid("123456"));
        assertTrue(isTextFieldValid("qwerty"));
        assertTrue(isTextFieldValid("asdfg"));
    }

    @Test
    public void invalidEmailTest() {
        assertFalse(isEmailValid("artemi.lyah"));
        assertFalse(isEmailValid(""));
        assertFalse(isEmailValid(null));
        assertFalse(isEmailValid("   "));
        assertFalse(isEmailValid("artemi.lyah.com"));
        assertFalse(isEmailValid("@mail.ru"));
        assertFalse(isEmailValid("  @mail.ru"));
        assertFalse(isEmailValid(".r@2mail"));
        assertFalse(isEmailValid("lyah.@mail.ru"));
    }

    @Test
    public void validEmailTest() {
        assertTrue(isEmailValid("lyah.artem10@mail.ru"));
        assertTrue(isEmailValid("Lyah.artem10@mail.ru"));
        assertTrue(isEmailValid("lyah@mail.ru"));
        assertTrue(isEmailValid("lyah_@mail.ru"));
        assertTrue(isEmailValid("artemi.lyah@mail.ru.com"));
    }
}
