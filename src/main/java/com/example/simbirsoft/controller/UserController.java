package com.example.simbirsoft.controller;

import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.service.user.UserService;
import com.example.simbirsoft.transfer.auth.SignUpDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public String createUser(@ModelAttribute SignUpDTO request, Model model){
        try {
            userService.createUser(request);
            return "redirect:/auth/log-in";
        } catch (ValidatorException exception) {
            model.addAttribute("error", exception.getMessage());
            return "/sign-up";
        }
    }
}
