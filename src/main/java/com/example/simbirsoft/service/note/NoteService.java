package com.example.simbirsoft.service.note;

import com.example.simbirsoft.transfer.note.RequestNoteDTO;
import com.example.simbirsoft.transfer.note.ResponseNoteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    ResponseNoteDTO findUserNote(long noteId, String email);
    List<ResponseNoteDTO> findUserNotes(int page, String email);
    List<ResponseNoteDTO> findUserNotes(String email);
    int getPageAmount(String email);
    void addUserNote(long noteId, RequestNoteDTO noteDTO);
    void updateUserNote(long noteId, RequestNoteDTO noteDTO, String email);
    void deleteUserNote(long noteId, String email);
}
