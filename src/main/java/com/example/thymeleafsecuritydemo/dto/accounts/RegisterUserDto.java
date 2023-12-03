package com.example.thymeleafsecuritydemo.dto.accounts;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterUserDto {

    @Size(min = 3, max = 30, message = "username is incorrect")
    @NotEmpty
    private String username;

    @Size(min = 3, max = 60, message = "email is incorrect")
    @Email
    @NotEmpty
    private String email;

    @Pattern(regexp = "^(?=.*[a-z]+)(?=.*[A-Z]+)(?=.*[0-9]+)(?=.*[@#$!%*^*?&_]+).{8,20}$", message = "password must be 8-20 characters long, contain letters, numbers, special characters, and must not contain spaces or emoji")
    private String password1;

    @Pattern(regexp = "^(?=.*[a-z]+)(?=.*[A-Z]+)(?=.*[0-9]+)(?=.*[@#$!%*^*?&_]+).{8,20}$", message = "password must be 8-20 characters long, contain letters, numbers, special characters, and must not contain spaces or emoji")
    private String password2;
}
