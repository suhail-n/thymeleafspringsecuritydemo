package com.example.thymeleafsecuritydemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurePagesController {
    @GetMapping(path = "/securepage", name = "securepage")
    public String getLogin() {
        return "securepages/secure_page";
    }
}
