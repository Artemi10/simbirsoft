package com.example.simbirsoft.service.note;

import com.example.simbirsoft.entity.Note;
import com.example.simbirsoft.entity.User;
import com.example.simbirsoft.repository.NoteRepository;
import com.example.simbirsoft.transfer.note.NoteDTO;
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
    public List<NoteDTO> findUserNotes(long userId, int page) {
        var pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return noteRepository.findAllByUserId(userId, pageable).stream()
                .map(NoteDTO::new)
                .toList();
    }

    @Override
    public List<NoteDTO> findUserNotes(long userId) {
        return findUserNotes(userId, 0);
    }

    @Override
    public int getPageAmount(long userId) {
        var noteAmount = noteRepository.getUserNotesAmount(userId);
        if (noteAmount > 0 && noteAmount % PAGE_SIZE == 0) {
            return noteAmount / PAGE_SIZE;
        }
        else {
            return noteAmount / PAGE_SIZE + 1;
        }
    }

    @Override
    public void addUserNote(long userId, NoteDTO noteDTO) {
        var user = User.builder().id(userId).build();
        var note = Note.builder()
                .text(noteDTO.text())
                .title(noteDTO.title())
                .user(user)
                .build();
        noteRepository.save(note);
    }
}
