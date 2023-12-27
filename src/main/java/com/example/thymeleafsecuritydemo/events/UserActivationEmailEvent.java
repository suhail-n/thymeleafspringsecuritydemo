package com.example.thymeleafsecuritydemo.events;

import com.example.thymeleafsecuritydemo.dto.accounts.UserDto;
import com.example.thymeleafsecuritydemo.models.UserEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserActivationEmailEvent extends ApplicationEvent {
    private final UserDto user;
    private final String applicationUrl;

    public UserActivationEmailEvent(UserDto user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
