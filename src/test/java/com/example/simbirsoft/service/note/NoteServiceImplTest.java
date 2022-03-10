package com.example.simbirsoft.service.note;

import com.example.simbirsoft.entity.Note;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.repository.NoteRepository;
import com.example.simbirsoft.transfer.note.RequestNoteDTO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NoteServiceImplTest {
    private final NoteRepository noteRepository;
    private final NoteService noteService;
    private List<Note> userNotes;

    @Autowired
    public NoteServiceImplTest() {
        this.noteRepository = spy(NoteRepository.class);
        this.noteService = new NoteServiceImpl(noteRepository);
    }

    @BeforeEach
    public void initNotes() {
        userNotes = Lists.list(
                Note.builder()
                        .id(1)
                        .title("Лабораторная работа")
                        .text("Сделать лаборатоную работу по схемотехнике")
                        .creationTime(new Timestamp(new Date().getTime()))
                        .build(),
                Note.builder()
                        .id(2)
                        .title("Спортзал")
                        .text("Сходить в спортзал в пятницу")
                        .creationTime(new Timestamp(new Date().getTime()))
                        .build(),
                Note.builder()
                        .id(3)
                        .title("Магазин")
                        .text("Пойти в магазин за продуктами")
                        .creationTime(new Timestamp(new Date().getTime()))
                        .build(),
                Note.builder()
                        .id(4)
                        .title("Проект")
                        .text("Доделать проект до субботы")
                        .creationTime(new Timestamp(new Date().getTime()))
                        .build()
        );
    }

    @BeforeEach
    public void initMock() {
        when(noteRepository.getUserNotesAmount("lyah.artem10@mail.ru"))
                .thenReturn(userNotes.size());
        when(noteRepository.getUserNotesAmount(argThat(email -> !email.equals("lyah.artem10@mail.ru"))))
                .thenReturn(0);
        when(noteRepository.findUserNoteById(1, "lyah.artem10@mail.ru"))
                .thenReturn(Optional.of(userNotes.get(0)));
        when(noteRepository.findUserNoteById(anyInt(), argThat(email -> !email.equals("lyah.artem10@mail.ru"))))
                .thenReturn(Optional.empty());
        when(noteRepository.save(any(Note.class)))
                .thenAnswer(answer -> {
                    var note = (Note) answer.getArgument(0);
                    return Note.builder()
                            .id(5)
                            .title(note.getTitle())
                            .text(note.getText())
                            .creationTime(note.getCreationTime())
                            .build();
                });
    }

    @Test
    public void getPageAmount_From_User_Notes_Test() {
        assertEquals(2, noteService.getPageAmount("lyah.artem10@mail.ru"));
        verify(noteRepository, times(1))
                .getUserNotesAmount("lyah.artem10@mail.ru");
    }

    @Test
    public void getPageAmount_From_Empty_User_Notes_Test() {
        assertEquals(1, noteService.getPageAmount("lyah.artem10@gmail.com"));
        verify(noteRepository, times(1))
                .getUserNotesAmount("lyah.artem10@gmail.com");
    }

    @Test
    public void add_New_User_Note_When_Note_Is_Invalid() {
        var userId = 6;
        var correctDTO = new RequestNoteDTO("Покупки", "Сходить в магазин");
        assertDoesNotThrow(() -> noteService.addUserNote(userId, correctDTO));
        verify(noteRepository, times(1))
                .save(argThat(note -> note.getTitle().equals(correctDTO.title())
                            && note.getText().equals(correctDTO.text())
                            && note.getUser().getId() == userId)
                );
    }

    @Test
    public void throw_exception_When_NoteDTO_Is_Invalid() {
        var userId = 6;
        var correctDTO = new RequestNoteDTO("", "Сходить в магазин");
        var exception = assertThrows(ValidatorException.class, () -> noteService.addUserNote(userId, correctDTO));
        assertEquals("Введён некорректный заголовок", exception.getMessage());
        verify(noteRepository, times(0)).save(any());
    }

    @Test
    public void find_User_Note_If_Exists() {
        var noteId = 1;
        var email = "lyah.artem10@mail.ru";
        var response = assertDoesNotThrow(() -> noteService.findUserNote(noteId, email));
        assertEquals(noteId, response.id());
        assertEquals("Лабораторная работа", response.title());
        assertEquals("Сделать лаборатоную работу по схемотехнике", response.text());
        verify(noteRepository, times(1)).findUserNoteById(noteId, email);
    }

    @Test
    public void throw_Exception_When_Find_Not_Existent_Note() {
        var exception = assertThrows(EntityException.class,
                () -> noteService.findUserNote(1, "lyah.artem03@mail.ru"));
        assertEquals("Записки не существует", exception.getMessage());
        verify(noteRepository, times(1)).findUserNoteById(1, "lyah.artem03@mail.ru");
        exception = assertThrows(EntityException.class,
                () -> noteService.findUserNote(10, "lyah.artem03@mail.ru"));
        assertEquals("Записки не существует", exception.getMessage());
        verify(noteRepository, times(1)).findUserNoteById(10, "lyah.artem03@mail.ru");
    }

    @Test
    public void update_User_Note_If_Exists() {
        var noteId = 1;
        var email = "lyah.artem10@mail.ru";
        var noteDTO = new RequestNoteDTO("Лабароторная работа 5", "Сделать лабаротоную работу по схемотехнике до четверга");
        assertDoesNotThrow(() -> noteService.updateUserNote(noteId, noteDTO, email));
        verify(noteRepository, times(1))
                .save(argThat(note -> note.getId() == noteId
                        && note.getText().equals(noteDTO.text())
                        && note.getTitle().equals(noteDTO.title()))
                );
    }

    @Test
    public void throw_Exception_When_Update_Not_Existent_Note() {
        var noteDTO = new RequestNoteDTO("Лабароторная работа 5", "Сделать лабаротоную работу по схемотехнике до четверга");
        var exception = assertThrows(EntityException.class,
                () -> noteService.updateUserNote(1, noteDTO, "lyah.artem03@mail.ru"));
        assertEquals("Записки не существует", exception.getMessage());
        verify(noteRepository, times(1)).findUserNoteById(1, "lyah.artem03@mail.ru");
        exception = assertThrows(EntityException.class,
                () -> noteService.updateUserNote(10, noteDTO, "lyah.artem03@mail.ru"));
        assertEquals("Записки не существует", exception.getMessage());
        verify(noteRepository, times(1)).findUserNoteById(10, "lyah.artem03@mail.ru");
    }

    @Test
    public void throw_Exception_When_Update_Invalid_Note() {
        var noteDTO1 = new RequestNoteDTO("", "Сделать лабаротоную работу по схемотехнике до четверга");
        var exception = assertThrows(ValidatorException.class,
                () -> noteService.updateUserNote(1, noteDTO1, "lyah.artem10@mail.ru"));
        assertEquals("Введён некорректный заголовок", exception.getMessage());
        verify(noteRepository, times(0)).findUserNoteById(anyInt(), anyString());

        var noteDTO2 = new RequestNoteDTO("Лабароторная работа 5", " ");
        exception = assertThrows(ValidatorException.class,
                () -> noteService.updateUserNote(10, noteDTO2, "lyah.artem03@mail.ru"));
        assertEquals("Введён некорректный текст", exception.getMessage());
        verify(noteRepository, times(0)).findUserNoteById(anyInt(), anyString());
    }
}
