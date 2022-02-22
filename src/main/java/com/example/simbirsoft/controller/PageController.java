package com.example.simbirsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping
    public String showMainPage(){
        return "main";
    }

    @GetMapping("/sign-up")
    public String showSignUpPage(){
        return "sign-up";
    }

    @GetMapping("/log-in")
    public String showLogInPage(){
        return "log-in";
    }

    @GetMapping("/add/note/user/{userId}")
    public String showCreateNoteForm(@PathVariable long userId, Model model) {
        model.addAttribute("userId", userId);
        return "create";
    }
}
