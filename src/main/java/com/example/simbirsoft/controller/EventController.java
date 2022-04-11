package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.security.details.SecureUser;
import com.example.simbirsoft.service.event.EventService;
import com.example.simbirsoft.transfer.event.EventRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/app")
public class EventController {
    private final EventService eventService;

    @PostMapping("/{appId}/event")
    public String addAppEvent(@PathVariable long appId, @ModelAttribute EventRequestDTO request,
                              Authentication authentication, Model model){
        try {
            var email = ((SecureUser) authentication.getPrincipal()).email();
            eventService.addEvent(appId, request, email);
            return String.format("redirect:/app/%d/stat?type=months&from=&to=", appId);
        } catch (Exception exception){
            model.addAttribute("appId", appId);
            model.addAttribute("error", exception.getMessage());
            return "event/create";
        }
    }

    @GetMapping("/{appId}/event")
    public String showCreateEventPage(@PathVariable long appId, Model model){
        model.addAttribute("appId", appId);
        return "event/create";
    }
}
