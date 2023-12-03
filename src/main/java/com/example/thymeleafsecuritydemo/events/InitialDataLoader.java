package com.example.thymeleafsecuritydemo.events;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.thymeleafsecuritydemo.dto.accounts.RegisterUserDto;
import com.example.thymeleafsecuritydemo.models.Privilege;
import com.example.thymeleafsecuritydemo.models.Role;
import com.example.thymeleafsecuritydemo.models.UserEntity;
import com.example.thymeleafsecuritydemo.repository.PrivilegeRepository;
import com.example.thymeleafsecuritydemo.repository.RoleRepository;
import com.example.thymeleafsecuritydemo.repository.UserRepository;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(UserRepository userRepository, RoleRepository roleRepository,
                             PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // The ContextRefreshedEvent may be fired multiple times depending on how many
        // contexts we have configured
        // in our application. And we only want to run the setup once.
        if (alreadySetup)
            return;
        Privilege readPrivilege = createPrivilegeIfNotExist("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotExist("WRITE_PRIVILEGE");
        // ROLE_ prefix will be added by spring
        createRoleIfNotExist("ADMIN", Arrays.asList(readPrivilege, writePrivilege));
        createRoleIfNotExist("USER", List.of(readPrivilege));

        // ROLE_ prefix will be added by spring
        createUserIfNotExist("user", "user@email.com", "password", "USER");
        createUserIfNotExist("admin", "admin@email.com", "password", "ADMIN");
    }

    private void createUserIfNotExist(String username, String email, String password, String roleName) {
        this.userRepository.findByUsername(username)
                .or(
                        () -> this.roleRepository
                                .findByName(roleName)
                                .map(
                                        role -> UserEntity
                                                .builder()
                                                .username(username)
                                                .email(email)
                                                .password(this.passwordEncoder.encode(password))
                                                .roles(List.of(role)).build())
                                .map(this.userRepository::save)
                );
    }

    private void createRoleIfNotExist(String roleName, Collection<Privilege> privileges) {
        this.roleRepository.findByName(roleName).or(
                () -> Optional.of(Role
                        .builder()
                        .name(roleName)
                        .privileges(privileges)
                        .build())
        ).map(this.roleRepository::save);
    }

    private Privilege createPrivilegeIfNotExist(String privilegeName) {
        return this.privilegeRepository.findByName(privilegeName).or(
                        () -> Optional.of(Privilege
                                .builder()
                                .name(privilegeName)
                                .build())
                ).map(this.privilegeRepository::save)
                .get();
    }

}
