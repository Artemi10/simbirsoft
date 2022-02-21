package com.example.simbirsoft.service.note;

import com.example.simbirsoft.entity.Note;
import com.example.simbirsoft.transfer.note.NoteDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    List<NoteDTO> findUserNotes(long userId, Pageable pageable);
    void addUserNote(long userId, NoteDTO noteDTO);
}
