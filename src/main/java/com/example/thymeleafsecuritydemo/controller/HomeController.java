package com.example.thymeleafsecuritydemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(name = "home")
    public String index(Model model) {
        model.addAttribute("today", "November 18, 2023");
        return "home/index";
    }
}
