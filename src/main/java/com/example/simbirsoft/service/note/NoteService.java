package com.example.simbirsoft.service.note;

import com.example.simbirsoft.transfer.note.NoteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    List<NoteDTO> findUserNotes(long userId, int page);
    List<NoteDTO> findUserNotes(long userId);
    int getPageAmount(long userId);
    void addUserNote(long userId, NoteDTO noteDTO);
}
