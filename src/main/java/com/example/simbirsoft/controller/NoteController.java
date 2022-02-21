package com.example.simbirsoft.controller;

import com.example.simbirsoft.entity.Note;
import com.example.simbirsoft.service.note.NoteService;
import com.example.simbirsoft.transfer.note.NoteDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/user/{userId}")
    public List<Note> findUserNotes(@PathVariable long userId, @RequestParam int page, @RequestParam int size) {
        var pageable = PageRequest.of(page, size);
        return noteService.findUserNotes(userId, pageable);
    }

    @PostMapping("/user/{userId}")
    public void createUserNote(@PathVariable long userId, @RequestBody NoteDTO noteDTO) {
        noteService.addUserNote(userId, noteDTO);
    }

}
