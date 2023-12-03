package com.example.thymeleafsecuritydemo.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.thymeleafsecuritydemo.dto.accounts.RegisterUserDto;
import com.example.thymeleafsecuritydemo.exception.RegistrationFailException;
import com.example.thymeleafsecuritydemo.models.Role;
import com.example.thymeleafsecuritydemo.models.UserEntity;
import com.example.thymeleafsecuritydemo.repository.RoleRepository;
import com.example.thymeleafsecuritydemo.repository.UserRepository;

@Service
public class AccountsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AccountsService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public boolean userByEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean userByUsernameExists(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    public Optional<UserEntity> createUser(RegisterUserDto registerUserDto) {
        return this.roleRepository.findByName("USER")
                .map( role -> UserEntity
                        .builder()
                        .email(registerUserDto.getEmail())
                        .username(registerUserDto.getUsername())
                        .password(this.passwordEncoder.encode(registerUserDto.getPassword1()))
                        .roles(Collections.singletonList(role))
                        .build())
                .map(this.userRepository::save);
    }
}
