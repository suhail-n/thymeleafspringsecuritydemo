package com.example.thymeleafsecuritydemo.security;

import com.example.thymeleafsecuritydemo.models.Privilege;
import com.example.thymeleafsecuritydemo.models.Role;
import com.example.thymeleafsecuritydemo.models.UserEntity;
import com.example.thymeleafsecuritydemo.repository.UserRepository;

import com.example.thymeleafsecuritydemo.service.AccountsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextListener;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HttpServletRequest request;

    private final AccountsService accountsService;

    public CustomUserDetailsService(UserRepository userRepository, HttpServletRequest request, AccountsService accountsService) {
        this.userRepository = userRepository;
        this.request = request;
        this.accountsService = accountsService;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username and password"));

        Collection<Role> roles = userEntity.getRoles();
        /**
         * Spring Security can take an overloaded method for authorities and roles in
         * the Users object.
         * In this example, take a string array of roles and privileges. The code will
         * convert them all to a list of
         * SimpleGrantedAuthority("role or privilege added here"). So they all get
         * treated as the same thing.
         * Spring secuirty can tell what is a privilege vs role by checking teh prefix
         * of the name.
         * Roles are automatically appended the "ROLE_" prefix when we set it with
         * builder.roles
         * Authorities/Priviletes will not be given the prefix of "ROLE_" and that's how
         * Spring Security distinguishes them.
         */
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> "ROLE_" + role.getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // add privileges to authorities
        authorities.addAll(
                roles.stream()
                        .map(Role::getPrivileges)
                        .flatMap(Collection::stream)
                        .map(Privilege::getName)
                        .map(SimpleGrantedAuthority::new)
                        .toList());

        if (!userEntity.isEnabled()) {
            this.accountsService.sendConfirmationEmail(userEntity, this.accountsService.applicationUrl(request));
        }

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(authorities)
                // .authorities(privilegeArray)
                // adding .roles will overwrite .authorities so I merged both together above
                // .roles(roleArray)
                // false by default but can check if email is activated before adding this.
                .disabled(!userEntity.isEnabled()).build();
    }

}
