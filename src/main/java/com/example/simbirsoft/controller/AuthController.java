package com.example.simbirsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/sign-up")
    public String showSignUpPage(){
        return "sign-up";
    }

    @GetMapping("/log-in")
    public String showLogInPage(){
        return "log-in";
    }
}
