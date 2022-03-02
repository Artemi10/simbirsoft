package com.example.simbirsoft.service.note;

import com.example.simbirsoft.entity.Note;
import com.example.simbirsoft.entity.User;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.repository.NoteRepository;
import com.example.simbirsoft.transfer.note.RequestNoteDTO;
import com.example.simbirsoft.transfer.note.ResponseNoteDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {
    private static final int PAGE_SIZE = 4;
    private final NoteRepository noteRepository;

    @Override
    public List<ResponseNoteDTO> findUserNotes(int page, String email) {
        var pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return noteRepository
                .findAllByUserEmail(email, pageable).stream()
                .map(note -> new ResponseNoteDTO(note.getId(), note.getTitle(), note.getText()))
                .toList();
    }

    @Override
    public List<ResponseNoteDTO> findUserNotes(String email) {
        return findUserNotes(1, email);
    }

    @Override
    public int getPageAmount(String email) {
        var noteAmount = noteRepository.getUserNotesAmount(email);
        if (noteAmount > 0 && noteAmount % PAGE_SIZE == 0) {
            return noteAmount / PAGE_SIZE;
        }
        else {
            return noteAmount / PAGE_SIZE + 1;
        }
    }

    @Override
    public void addUserNote(long noteId, RequestNoteDTO noteDTO) {
        noteDTO.check();
        var user = User.builder().id(noteId).build();
        var note = Note.builder()
                .text(noteDTO.text())
                .title(noteDTO.title())
                .user(user)
                .build();
        noteRepository.save(note);
    }

    @Override
    public ResponseNoteDTO findUserNote(long noteId, String email) {
        return noteRepository.findUserNoteById(noteId, email)
                .map(note -> new ResponseNoteDTO(note.getId(), note.getTitle(), note.getText()))
                .orElseThrow(() -> new EntityException("Записки не существует"));
    }

    @Override
    public void updateUserNote(long noteId, RequestNoteDTO noteDTO, String email) {
        noteDTO.check();
        var note = noteRepository.findUserNoteById(noteId, email)
                .orElseThrow(() -> new EntityException("Записки не существует"));
        note.setTitle(noteDTO.title());
        note.setText(noteDTO.text());
        noteRepository.save(note);
    }

    @Override
    public void deleteUserNote(long noteId, String email) {
        noteRepository.deleteUserNoteById(noteId, email);
    }
}
