package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.details.SecureUser;
import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.service.note.NoteService;
import com.example.simbirsoft.transfer.note.NoteDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public String findUserNotes(Authentication authentication, @RequestParam(defaultValue = "1") int page, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var notes = noteService.findUserNotes(email, page);
        var pageAmount = noteService.getPageAmount(email);
        model.addAttribute("notes", notes);
        model.addAttribute("pageAmount", pageAmount);
        model.addAttribute("currentPage", page);
        return "notes";
    }

    @PostMapping
    public String createUserNote(Authentication authentication, @ModelAttribute NoteDTO request, Model model) {
        try{
            var id = ((SecureUser) authentication.getPrincipal()).id();
            noteService.addUserNote(id, request);
            return "redirect:/notes";
        } catch (ValidatorException exception) {
            model.addAttribute("error", exception.getMessage());
            return "create";
        }
    }

    @GetMapping("/add")
    public String showCreateNoteForm() {
        return "create";
    }
}
