package com.example.simbirsoft.service.note;

import com.example.simbirsoft.entity.Note;
import com.example.simbirsoft.entity.User;
import com.example.simbirsoft.repository.NoteRepository;
import com.example.simbirsoft.transfer.note.NoteDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public List<Note> findUserNotes(long userId, Pageable pageable) {
        return noteRepository.findAllByUserId(userId, pageable).toList();
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
