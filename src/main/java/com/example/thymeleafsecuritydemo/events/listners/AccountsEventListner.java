package com.example.thymeleafsecuritydemo.events.listners;

import com.example.thymeleafsecuritydemo.events.UserActivationEmailEvent;
import com.example.thymeleafsecuritydemo.service.AccountsService;
import com.example.thymeleafsecuritydemo.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountsEventListner {

    private final AccountsService accountsService;
    private final EmailService emailService;

    public AccountsEventListner(AccountsService accountsService, EmailService emailService) {
        this.accountsService = accountsService;
        this.emailService = emailService;
    }

    @EventListener
    public void handleUserActivation(UserActivationEmailEvent userActivationEmailEvent) throws MessagingException {
        var user = userActivationEmailEvent.getUser();
        // Create the Verification Token for the User
        var token = UUID.randomUUID().toString();

        this.accountsService.saveActivationToken(user, token);
        var url = userActivationEmailEvent.getApplicationUrl()
                .concat("/registration/verify?token=")
                .concat(token);
        this.emailService.sendAccountVerification(user, url);
    }
}
