package com.example.thymeleafsecuritydemo.events;

import com.example.thymeleafsecuritydemo.models.UserEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserActivationEmailEvent extends ApplicationEvent {
    private final UserEntity user;
    private final String applicationUrl;

    public UserActivationEmailEvent(UserEntity user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
