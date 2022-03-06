package com.example.simbirsoft.controller;

import com.example.simbirsoft.exception.EmailException;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.service.email.MessageSender;
import com.example.simbirsoft.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Component
@AllArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final UserService userService;
    private final MessageSender messageSender;

    @PostMapping
    public String sendEmail(@RequestParam String email, Model model) {
        var resetToken = UUID.randomUUID().toString();
        try {
            userService.resetUser(email, resetToken);
            messageSender.sendMessage(email, resetToken);
            return "/auth/reset-info";
        } catch (EntityException exception) {
            model.addAttribute("error", exception.getMessage());
            return "/auth/email";
        } catch (EmailException exception) {
            userService.activateUser(email);
            model.addAttribute("error", exception.getMessage());
            return "/auth/email";
        }
    }
}
