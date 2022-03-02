package com.example.simbirsoft.service.note;

import com.example.simbirsoft.transfer.note.NoteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    NoteDTO findUserNote(long noteId, String email);
    List<NoteDTO> findUserNotes(int page, String email);
    List<NoteDTO> findUserNotes(String email);
    int getPageAmount(String email);
    void addUserNote(long noteId, NoteDTO noteDTO);
    void updateUserNote(long noteId, NoteDTO noteDTO, String email);
    void deleteUserNote(long noteId, String email);
}
