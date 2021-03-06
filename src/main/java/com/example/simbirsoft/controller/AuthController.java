package com.example.simbirsoft.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/log-in")
    public String showLogInPage(@RequestParam(defaultValue = "false") boolean error, Model model, Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()){
            if (error){
                var errorMessage = "Введён неверный логин или пароль";
                model.addAttribute("error", errorMessage);
            }
            return "auth/log-in";
        }
        else {
            return "redirect:/";
        }
    }

    @GetMapping("/sign-up")
    public String showSignUpPage(){
        return "auth/sign-up";
    }

    @GetMapping("/email")
    public String showEmailPage(){
        return "auth/email";
    }
}
