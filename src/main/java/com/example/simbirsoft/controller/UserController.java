package com.example.simbirsoft.controller;

import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.service.user.UserService;
import com.example.simbirsoft.transfer.auth.SignUpDTO;
import com.example.simbirsoft.transfer.auth.LogInDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/log-in")
    public String logIn(@ModelAttribute LogInDTO request, Model model){
        try {
            userService.logIn(request);
            return "main";
        } catch (ValidatorException exception) {
            model.addAttribute("error", exception.getMessage());
            return "log-in";
        }
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute SignUpDTO request, Model model){
        try {
            userService.signUp(request);
            return "main";
        } catch (ValidatorException exception) {
            model.addAttribute("error", exception.getMessage());
            return "sign-up";
        }
    }
}
