package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.security.details.SecureUser;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.service.stat.StatService;
import com.example.simbirsoft.transfer.stat.StatRequestDTO;
import com.example.simbirsoft.transfer.stat.StatResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller
@AllArgsConstructor
@RequestMapping("/app")
public class StatController {
    private final Map<String, StatService> statServices;

    @GetMapping("/{appId}/stat")
    public String createStats(@PathVariable long appId, @RequestParam String type,
                              @RequestParam String from, @RequestParam String to,
                              Authentication authentication, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();

        List<StatResponseDTO> stats;
        if (!from.isBlank() && !to.isBlank()) {
            try {
                var requestDTO = new StatRequestDTO(email, from, to);
                stats = statServices.containsKey(type) ?
                        statServices.get(type).createStats(appId, requestDTO) : Collections.emptyList();
            } catch (ParseException | EntityException exception) {
                stats = Collections.emptyList();
            }
        }
        else {
            try {
                stats = statServices.containsKey(type) ?
                        statServices.get(type).createStats(appId, email) : Collections.emptyList();
            } catch (EntityException exception) {
                    stats = Collections.emptyList();
            }
        }

        model.addAttribute("appId", appId);
        model.addAttribute("stats", stats);
        return "event/events";
    }
}
