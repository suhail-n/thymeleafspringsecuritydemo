package com.example.thymeleafsecuritydemo.service;

import com.example.thymeleafsecuritydemo.dto.accounts.UserDto;
import com.example.thymeleafsecuritydemo.models.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final TemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void sendAccountVerification(UserDto user, String activationUrl) throws MessagingException {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("activationUrl", activationUrl);

        String process = templateEngine.process("emails/activation", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setSubject("Welcome " + user.getUsername());
        messageHelper.setText(process, true);
        messageHelper.setTo(user.getEmail());
        messageHelper.setFrom("noreply@myapp.com");
        javaMailSender.send(mimeMessage);
    }
}
