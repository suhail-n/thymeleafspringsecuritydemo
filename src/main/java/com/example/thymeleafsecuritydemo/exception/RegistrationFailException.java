package com.example.thymeleafsecuritydemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class RegistrationFailException extends Exception {
    private static final long serialVersionUID = -3332292346834265371L;

    public RegistrationFailException(String message) {
        super(message);
    }
}
