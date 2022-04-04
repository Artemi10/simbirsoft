package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.security.details.SecureUser;
import com.example.simbirsoft.service.stat.StatService;
import com.example.simbirsoft.transfer.stat.StatRequestDTO;
import com.example.simbirsoft.transfer.stat.StatResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;


@Controller
@AllArgsConstructor
@RequestMapping("/app")
public class StatController {
    private final Map<String, StatService> statServices;

    @GetMapping("/{appId}/stat/hours/current")
    public String createStatByMonths(@PathVariable long appId, Authentication authentication, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var stats = statServices.containsKey("hourStatService") ?
                statServices.get("hourStatService").createStats(appId, email)
                : Collections.emptyList();
        model.addAttribute("stats", stats);
        return "event/events";
    }

    @GetMapping("/{appId}/stat/hours")
    public String createStatByHours(@PathVariable long appId,
                                    @RequestParam Timestamp from, @RequestParam Timestamp to,
                                    Authentication authentication, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var requestDTO = new StatRequestDTO(email, from, to);
        var stats = statServices.containsKey("hourStatService") ?
                statServices.get("hourStatService").createStats(appId, requestDTO)
                : Collections.emptyList();
        model.addAttribute("stats", stats);
        return "event/events";
    }

    @GetMapping("/{appId}/stat/months/current")
    public String createStatByHours(@PathVariable long appId, Authentication authentication, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var stats = statServices.containsKey("monthStatService") ?
                statServices.get("monthStatService").createStats(appId, email)
                : Collections.emptyList();
        model.addAttribute("stats", stats);
        return "event/events";
    }

    @GetMapping("/{appId}/stat/months")
    public String createStatByMonths(@PathVariable long appId,
                                    @RequestParam Timestamp from, @RequestParam Timestamp to,
                                    Authentication authentication, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var requestDTO = new StatRequestDTO(email, from, to);
        var stats = statServices.containsKey("monthStatService") ?
                statServices.get("monthStatService").createStats(appId, requestDTO)
                : Collections.emptyList();
        model.addAttribute("stats", stats);
        return "event/events";
    }

    @GetMapping("/{appId}/stat/days/current")
    public String createStatByDays(@PathVariable long appId, Authentication authentication, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var stats = statServices.containsKey("dayStatService") ?
                statServices.get("dayStatService").createStats(appId, email)
                : Collections.emptyList();
        model.addAttribute("stats", stats);
        return "event/events";
    }

    @GetMapping("/{appId}/stat/days")
    public String createStatByDays(@PathVariable long appId,
                                   @RequestParam Timestamp from, @RequestParam Timestamp to,
                                   Authentication authentication, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var requestDTO = new StatRequestDTO(email, from, to);
        var stats = statServices.containsKey("dayStatService") ?
                statServices.get("dayStatService").createStats(appId, requestDTO)
                : Collections.emptyList();
        model.addAttribute("stats", stats);
        return "event/events";
    }
}
