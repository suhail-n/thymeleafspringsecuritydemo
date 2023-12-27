package com.example.thymeleafsecuritydemo.controller;

import com.example.thymeleafsecuritydemo.dto.accounts.RegisterUserDto;
import com.example.thymeleafsecuritydemo.exception.RegistrationFailException;
import com.example.thymeleafsecuritydemo.service.AccountsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService, ApplicationEventPublisher applicationEventPublisher) {
        this.accountsService = accountsService;
    }

    @GetMapping(path = "/login", name = "account_login")
    public String getLogin() {
        return "accounts/login";
    }

    @GetMapping(path = "/signup", name = "account_signup")
    public String getSignup(Model model, @RequestParam(required = false) String error) {
        var user = new RegisterUserDto();
        model.addAttribute("user", user);
        if (error != null) {
            model.addAttribute("error", "Something went wrong. Contact Support.");
        }
        return "accounts/signup";
    }

    @PostMapping(path = "/signup", name = "account_signup_post")
    public String signup(@Valid @ModelAttribute("user") RegisterUserDto registerUserDto, BindingResult result,
            Model model, final HttpServletRequest request) {

        // check database if username or email exists
        var usernameError = result.getFieldError("username");
        if (usernameError == null && this.accountsService.userByUsernameExists(registerUserDto.getUsername())) {
            result.addError(new FieldError("user", "username", "username is already in use"));
        }
        var emailError = result.getFieldError("email");
        if (emailError == null && this.accountsService.userByEmailExists(registerUserDto.getEmail())) {
            result.addError(new FieldError("user", "email", "email is already in use"));
        }

        if (!registerUserDto.getPassword1().equals(registerUserDto.getPassword2())) {
            result.addError(new FieldError("user", "password1", "passwords do not match"));
            result.addError(new FieldError("user", "password2", "passwords do not match"));
        }
        if (result.hasErrors()) {
            // List<FieldError> err = result.getFieldErrors();

            // for (FieldError e : err) {
            // System.out.println("Error on object ---> " + e.getObjectName() + " on
            // field---> " + e.getField()
            // + ". Message ---> " + e.getDefaultMessage());
            // }
            // model.addAttribute("user", registerUserDto);
            return "accounts/signup";
        }
        return this.accountsService
                .createUser(registerUserDto)
                .map(userEntity -> {
                    this.accountsService.sendConfirmationEmail(userEntity,
                            this.accountsService.applicationUrl(request));
                    return "accounts/verification_sent";
                }).orElseGet(() -> {
                    model.addAttribute("error", "Something went wrong. Please contact support.");
                    return "accounts/signup";
                });
    }

    @GetMapping(path = "/password/reset", name = "account_reset_password")
    public String getPasswordReset() {
        return "accounts/password_reset";
    }

    @GetMapping(path = "/registration/verify", name = "account_verify_email")
    public String getEmailVerification(@RequestParam("token") Optional<String> token, Model model) {
        model.addAttribute("isConfirmed", false);
        token
                .flatMap(this.accountsService::activateUserByToken)
                .map(userEntity -> model.addAttribute("isConfirmed", true));
        return "accounts/email_confirmation";
    }

    @ExceptionHandler(RegistrationFailException.class)
    public ModelAndView handleRegistrationFailException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView("redirect:/signup");
        modelAndView.addObject("error", "");
        return modelAndView;
    }

}