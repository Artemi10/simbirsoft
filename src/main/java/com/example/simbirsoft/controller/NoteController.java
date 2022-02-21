package com.example.simbirsoft.controller;

import com.example.simbirsoft.service.note.NoteService;
import com.example.simbirsoft.transfer.note.NoteDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private static final int PAGE_SIZE = 4;
    private final NoteService noteService;

    @GetMapping("/user/{userId}")
    public String findUserNotes(@PathVariable long userId, @RequestParam int page, Model model) {
        var pageable = PageRequest.of(page, PAGE_SIZE);
        var notes = noteService.findUserNotes(userId, pageable);
        model.addAttribute("notes", notes);
        return "notes";
    }

    @PostMapping("/user/{userId}")
    public String createUserNote(@PathVariable long userId, @RequestBody NoteDTO noteDTO, Model model) {
        noteService.addUserNote(userId, noteDTO);
        var pageable = PageRequest.of(1, PAGE_SIZE);
        var notes = noteService.findUserNotes(userId, pageable);
        model.addAttribute("notes", notes);
        return "notes";
    }
}
