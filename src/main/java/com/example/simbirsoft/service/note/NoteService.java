package com.example.simbirsoft.service.note;

import com.example.simbirsoft.transfer.note.NoteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    List<NoteDTO> findUserNotes(String email, int page);
    List<NoteDTO> findUserNotes(String email);
    int getPageAmount(String email);
    void addUserNote(long id, NoteDTO noteDTO);
}
