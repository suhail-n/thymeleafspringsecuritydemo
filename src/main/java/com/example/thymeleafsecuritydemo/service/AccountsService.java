package com.example.thymeleafsecuritydemo.service;

import com.example.thymeleafsecuritydemo.dto.accounts.RegisterUserDto;
import com.example.thymeleafsecuritydemo.dto.accounts.UserDto;
import com.example.thymeleafsecuritydemo.events.UserActivationEmailEvent;
import com.example.thymeleafsecuritydemo.mapper.UserMapper;
import com.example.thymeleafsecuritydemo.models.Activation;
import com.example.thymeleafsecuritydemo.models.UserEntity;
import com.example.thymeleafsecuritydemo.repository.ActivationRepository;
import com.example.thymeleafsecuritydemo.repository.RoleRepository;
import com.example.thymeleafsecuritydemo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class AccountsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ActivationRepository activationRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public AccountsService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, ApplicationEventPublisher applicationEventPublisher,
                           ActivationRepository activationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.activationRepository = activationRepository;
    }

    public boolean userByEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean userByUsernameExists(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    public Optional<UserEntity> createUser(RegisterUserDto registerUserDto) {
        return this.roleRepository.findByName("USER")
                .map(role -> UserEntity
                        .builder()
                        .email(registerUserDto.getEmail())
                        .username(registerUserDto.getUsername())
                        .password(this.passwordEncoder.encode(registerUserDto.getPassword1()))
                        .roles(Collections.singletonList(role))
                        .build())
                .map(this.userRepository::save);
    }

    public void sendConfirmationEmail(UserEntity userEntity, String url) {
        this.applicationEventPublisher.publishEvent(
                new UserActivationEmailEvent(
                        UserMapper.mapToUserDto(userEntity), url));
    }

    @Transactional
    public Optional<UserEntity> saveActivationToken(UserDto userDto, String token) {
        Optional<UserEntity> foundUser = this.userRepository.findByUsername(userDto.getUsername());
        return foundUser
                .map(UserEntity::getActivation)
                .map(activation -> {
                    activation.setToken(token);
                    return activation;
                })
                .map(this.activationRepository::save)
                .flatMap((activation) -> foundUser)
                .or(() -> {
                    var activation = Activation.builder().token(token).build();
                    Activation savedActivation = this.activationRepository.save(activation);
                    return foundUser
                            .map(
                                    userEntity -> {
                                        userEntity.setActivation(savedActivation);
                                        return this.userRepository.save(userEntity);
                                    });
                });
    }

    @Transactional
    public Optional<UserEntity> activateUserByToken(String token) {
        return this.activationRepository
                .findByToken(token)
                .filter(Activation::isActive)
                .map(activation -> {
                    activation.setExpirationTime(new Date());
                    return this.activationRepository.save(activation);
                })
                .map(Activation::getToken)
                .flatMap(this.userRepository::findByActivationToken)
                .map(userEntity -> {
                    userEntity.setEnabled(true);
                    return userEntity;
                })
                .map(this.userRepository::save);
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
