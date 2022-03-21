package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.details.SecureUser;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.service.note.NoteService;
import com.example.simbirsoft.transfer.note.RequestNoteDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;


@Controller
@AllArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public String findUserNotes(Authentication authentication, @RequestParam(defaultValue = "1") int page, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var notes = noteService.findUserNotes(page, email);
        var pageAmount = noteService.getPageAmount(email);
        model.addAttribute("notes", notes);
        model.addAttribute("pageAmount", pageAmount);
        model.addAttribute("currentPage", page);
        return "note/notes";
    }

    @PostMapping
    public String createUserNote(Authentication authentication, @ModelAttribute RequestNoteDTO request, Model model) {
        try{
            var id = ((SecureUser) authentication.getPrincipal()).id();
            noteService.addUserNote(id, request);
            return "redirect:/notes";
        } catch (ValidatorException exception) {
            model.addAttribute("error", exception.getMessage());
            return "note/create";
        }
    }

    @PostMapping("/{noteId}/update")
    public String updateUserNote(Authentication authentication, @PathVariable long noteId, @ModelAttribute RequestNoteDTO request, Model model) {
        try {
            var email = ((SecureUser) authentication.getPrincipal()).email();
            noteService.updateUserNote(noteId, request, email);
            return "redirect:/notes";
        } catch (ValidatorException | EntityException exception) {
            model.addAttribute("note", request);
            return "note/update";
        }
    }

    @GetMapping("/{noteId}/delete")
    public String deleteUserNote(Authentication authentication, @PathVariable long noteId) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        noteService.deleteUserNote(noteId, email);
        return "redirect:/notes";
    }

    @GetMapping("/{noteId}/update")
    public String showUpdateNoteForm(Authentication authentication, @PathVariable long noteId, Model model) {
        try {
            var email = ((SecureUser) authentication.getPrincipal()).email();
            var note = noteService.findUserNote(noteId, email);
            model.addAttribute("note", note);
            return "note/update";
        } catch (EntityException exception) {
            return "redirect:/notes";
        }
    }

    @GetMapping("/add")
    public String showCreateNoteForm() {
        return "note/create";
    }
}
