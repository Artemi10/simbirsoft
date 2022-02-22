package com.example.simbirsoft.controller;

import com.example.simbirsoft.exception.ValidatorException;
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
    public String createUserNote(@PathVariable long userId, @ModelAttribute NoteDTO request, Model model) {
        try{
            noteService.addUserNote(userId, request);
            var notes = noteService.findUserNotes(userId);
            var pageAmount = noteService.getPageAmount(userId);
            model.addAttribute("notes", notes);
            model.addAttribute("pageAmount", pageAmount);
            model.addAttribute("currentPage", 1);
            model.addAttribute("userId", userId);
            return "notes";
        } catch (ValidatorException exception) {
            model.addAttribute("error", exception.getMessage());
            return "create";
        }
    }

    @GetMapping("/add/user/{userId}")
    public String showCreateNoteForm(@PathVariable long userId, Model model) {
        model.addAttribute("userId", userId);
        return "create";
    }
}
