package com.example.simbirsoft.service.note;

import com.example.simbirsoft.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NoteServiceImplTest {
    private final NoteRepository noteRepository;
    private final NoteService noteService;

    @Autowired
    public NoteServiceImplTest() {
        this.noteRepository = spy(NoteRepository.class);
        this.noteService = new NoteServiceImpl(noteRepository);
    }

    @BeforeEach
    public void initMock() {

    }

    @Test
    public void findUserNoteTest() {

    }
}
