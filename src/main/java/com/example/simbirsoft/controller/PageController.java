package com.example.simbirsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping
    public String showMainPage(){
        return "main";
    }
}
