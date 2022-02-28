package com.example.simbirsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/sign-up")
    public String showSignUpPage(){
        return "sign-up";
    }

    @GetMapping("/log-in")
    public String showLogInPage(@RequestParam(defaultValue = "false") boolean error, Model model){
        if (error){
            var errorMessage = "Введён неверный логин или пароль";
            model.addAttribute("error", errorMessage);
        }
        return "log-in";
    }
}
