package com.example.thymeleafsecuritydemo.dto.accounts;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    @Size(min = 3, max = 30, message = "username is incorrect")
    @NotEmpty
    private String username;

    @Size(min = 3, max = 60, message = "email is incorrect")
    @Email
    @NotEmpty
    private String email;
}
