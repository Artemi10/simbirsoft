package com.example.simbirsoft.controller;

import com.example.simbirsoft.transfer.auth.LogInDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    public String showLogInPage(@ModelAttribute LogInDTO logInDTO){
        return "log-in";
    }
}
