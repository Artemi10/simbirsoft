package com.example.simbirsoft.controller;

import com.example.simbirsoft.service.note.NoteService;
import com.example.simbirsoft.transfer.note.NoteDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/user/{userId}")
    public String findUserNotes(@PathVariable long userId, @RequestParam(defaultValue = "1") int page, Model model) {
        var notes = noteService.findUserNotes(userId, page);
        var pageAmount = noteService.getPageAmount(userId);
        model.addAttribute("notes", notes);
        model.addAttribute("pageAmount", pageAmount);
        model.addAttribute("currentPage", page);
        model.addAttribute("userId", userId);
        return "notes";
    }

    @PostMapping("/user/{userId}")
    public String createUserNote(@PathVariable long userId, @RequestBody NoteDTO noteDTO, Model model) {
        noteService.addUserNote(userId, noteDTO);
        var notes = noteService.findUserNotes(userId);
        var pageAmount = noteService.getPageAmount(userId);
        model.addAttribute("notes", notes);
        model.addAttribute("pageAmount", pageAmount);
        model.addAttribute("currentPage", 1);
        model.addAttribute("userId", userId);
        return "notes";
    }
}
