package com.example.simbirsoft.repository;

import com.example.simbirsoft.entity.user.Authority;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    public void findByEmail_When_User_Is_Active_Test(){
        var actual = assertDoesNotThrow(() -> userRepository.findByEmail("lyah.artem10@mail.ru").get());
        assertEquals(1, actual.getId());
        assertEquals("lyah.artem10@mail.ru", actual.getEmail());
        assertEquals("$2y$10$ZPgg5k.SQaJIxjGF7AU15.GNVF2U7MVJJWgMxkyuXjW550XIEEK52", actual.getPassword());
        assertEquals(Authority.ACTIVE, actual.getAuthority());
        assertNull(actual.getResetToken());
    }

    @Test
    public void findByEmail_When_User_Is_Reset_Test(){
        var actual = assertDoesNotThrow(() -> userRepository.findByEmail("d10@gmail.com").get());
        assertEquals(3, actual.getId());
        assertEquals("d10@gmail.com", actual.getEmail());
        assertEquals("$2y$10$x.jaNOvtBnsMqyhehZ5ituZzUAGnrHiSXzme1/i0EzrcWgRHMl0Ve", actual.getPassword());
        assertEquals(Authority.RESET, actual.getAuthority());
        assertEquals("1d6c4215-ceb3-4968-ac35-3456de6b4aa9", actual.getResetToken());
    }

    @Test
    public void findByEmail_When_User_Does_Not_Exist_Test(){
        var actual = userRepository.findByEmail("lyakh@mail.ru");
        assertTrue(actual.isEmpty());
    }
}
