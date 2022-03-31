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

    @GetMapping("/{appId}/events")
    public String showAppEvents(@PathVariable long appId, Authentication authentication, Model model){
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var events = eventService.findAppEvents(appId, email);
        model.addAttribute("appId", appId);
        model.addAttribute("events", events);
        return "event/events";
    }

    @PostMapping("/event")
    public String addAppEvent(@ModelAttribute EventRequestDTO requestBody, Authentication authentication){
        try {
            var email = ((SecureUser) authentication.getPrincipal()).email();
            eventService.addEvent(requestBody, email);
            return "redirect:/apps";
        } catch (Exception exception){
            return "redirect:/";
        }
    }
}
