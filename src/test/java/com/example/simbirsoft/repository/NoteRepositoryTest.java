package com.example.simbirsoft.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class NoteRepositoryTest {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteRepositoryTest(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @AfterEach
    public void tearDown(){
        noteRepository.deleteAll();
    }

    @Test
    public void findAllByUserEmail_Test(){
        var pageable = PageRequest.of(0, 3);
        var actual = noteRepository.findAllByUserEmail("lyah.artem10@mail.ru", pageable);
        assertEquals(3, actual.stream().count());
        pageable = PageRequest.of(1, 3);
        actual = noteRepository.findAllByUserEmail("lyah.artem10@mail.ru", pageable);
        assertEquals(1, actual.stream().count());
        pageable = PageRequest.of(2, 3);
        actual = noteRepository.findAllByUserEmail("lyah.artem10@mail.ru", pageable);
        assertEquals(0, actual.stream().count());
    }

    @Test
    public void findAllByUserEmail_When_NotesList_Is_Empty_Test(){
        var pageable = PageRequest.of(0, 3);
        var actual = noteRepository.findAllByUserEmail("lyah.artem10@gmail.com", pageable);
        assertEquals(0, actual.stream().count());
        pageable = PageRequest.of(1, 3);
        actual = noteRepository.findAllByUserEmail("lyah.artem10@gmail.com", pageable);
        assertEquals(0, actual.stream().count());
    }

    @Test
    public void findAllByUserEmail_When_User_Does_Not_Exist_Test(){
        var pageable = PageRequest.of(0, 3);
        var actual = noteRepository.findAllByUserEmail("lyah@gmail.com", pageable);
        assertEquals(0, actual.stream().count());
        pageable = PageRequest.of(1, 3);
        actual = noteRepository.findAllByUserEmail("lyah@gmail.com", pageable);
        assertEquals(0, actual.stream().count());
    }

    @Test
    public void getUserNotesAmount_Test(){
        var actual = noteRepository.getUserNotesAmount("lyah.artem10@mail.ru");
        assertEquals(4, actual);
    }

    @Test
    public void getUserNotesAmount_When_NotesList_Is_Empty_Test(){
        var actual = noteRepository.getUserNotesAmount("lyah.artem10@gmail.com");
        assertEquals(0, actual);
    }

    @Test
    public void getUserNotesAmount_When_User_Does_Not_Exist_Test(){
        var actual = noteRepository.getUserNotesAmount("lyah@gmail.com");
        assertEquals(0, actual);
    }

    @Test
    public void findUserNoteById_Test(){
        var actual = assertDoesNotThrow(() -> noteRepository
                .findUserNoteById(1, "lyah.artem10@mail.ru").get());
        assertEquals("Спортзал", actual.getTitle());
        assertEquals("Сходить в спортзал в четверг в 11:20", actual.getText());
        assertEquals("2022-03-13 03:14:07", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actual.getCreationTime()));
    }

    @Test
    public void findUserNoteById_When_User_Does_Not_Exist_Test(){
        var actual = noteRepository.findUserNoteById(1, "lyakh@mail.ru");
        assertTrue(actual.isEmpty());
    }

    @Test
    public void findUserNoteById_When_Note_Does_Not_Exist_Test(){
        var actual = noteRepository.findUserNoteById(7, "lyah.artem10@mail.ru");
        assertTrue(actual.isEmpty());
    }

    @Test
    public void deleteByIdAndUserEmail_Test(){
        noteRepository.deleteByIdAndUserEmail(1, "lyah.artem10@mail.ru");
        var actual = noteRepository.findUserNoteById(1, "lyah.artem10@mail.ru");
        assertTrue(actual.isEmpty());
    }

    @Test
    public void should_Not_DeleteByIdAndUserEmail_When_Email_Is_Incorrect_Test(){
        noteRepository.deleteByIdAndUserEmail(1, "lyah.artem10@gmail.com");
        var actual = noteRepository.findUserNoteById(1, "lyah.artem10@mail.ru");
        assertTrue(actual.isPresent());
    }
}
