package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.details.SecureUser;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.service.application.AppService;
import com.example.simbirsoft.transfer.application.AppRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/apps")
public class AppController {
    private final AppService appService;

    @GetMapping
    public String findUserApps(Authentication authentication, @RequestParam(defaultValue = "1") int page, Model model) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        var apps = appService.findUserApps(page, email);
        var pageAmount = appService.getPageAmount(email);
        model.addAttribute("apps", apps);
        model.addAttribute("pageAmount", pageAmount);
        model.addAttribute("currentPage", page);
        return "app/apps";
    }

    @PostMapping
    public String createUserApp(Authentication authentication, @ModelAttribute AppRequestDTO requestBody, Model model) {
        try{
            var id = ((SecureUser) authentication.getPrincipal()).id();
            appService.addUserApp(id, requestBody);
            return "redirect:/apps";
        } catch (ValidatorException exception) {
            model.addAttribute("error", exception.getMessage());
            return "app/create";
        }
    }

    @PostMapping("/{appId}/update")
    public String updateUserApp(Authentication authentication, @PathVariable long appId,
                                 @ModelAttribute AppRequestDTO requestBody, Model model) {
        try {
            var email = ((SecureUser) authentication.getPrincipal()).email();
            appService.updateUserApp(appId, requestBody, email);
            return "redirect:/apps";
        } catch (ValidatorException | EntityException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("appId", appId);
            model.addAttribute("app", requestBody);
            return "app/update";
        }
    }

    @GetMapping("/{appId}/delete")
    public String deleteUserApp(Authentication authentication, @PathVariable long appId) {
        var email = ((SecureUser) authentication.getPrincipal()).email();
        appService.deleteUserApp(appId, email);
        return "redirect:/apps";
    }

    @GetMapping("/{appId}/update")
    public String showUpdateAppForm(Authentication authentication, @PathVariable long appId, Model model) {
        try {
            var email = ((SecureUser) authentication.getPrincipal()).email();
            var app = appService.findUserApp(appId, email);
            model.addAttribute("app", app);
            return "app/update";
        } catch (EntityException exception) {
            return "redirect:/apps";
        }
    }

    @GetMapping("/add")
    public String showCreateAppForm() {
        return "app/create";
    }
}
