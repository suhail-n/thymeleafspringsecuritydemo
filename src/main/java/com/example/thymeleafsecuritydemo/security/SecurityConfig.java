package com.example.thymeleafsecuritydemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    private static String[] getPublicRoutes() {
        return new String[] { "/", "/registration/verify", "/login", "/signup", "/password/reset", "/css/**",
                "/js/**" };
    }

    public static MvcRequestMatcher[] getPublicMvcRoutes(HandlerMappingIntrospector introspector) {
        var req1 = new MvcRequestMatcher(introspector, "/");
        var req2 = new MvcRequestMatcher(introspector, "/login");
        var req3 = new MvcRequestMatcher(introspector, "/signup");
        var req4 = new MvcRequestMatcher(introspector, "/password/reset");
        var req5 = new MvcRequestMatcher(introspector, "/css/**");
        var req6 = new MvcRequestMatcher(introspector, "/js/**");
        var req7 = new MvcRequestMatcher(introspector, "/favicon.ico");
        return new MvcRequestMatcher[] { req1, req2, req3, req4, req5, req6, req7 };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        // .requestMatchers(mvc.pattern("/*")).permitAll()
                        // not needed toH2Console without h2 database
                        // .requestMatchers(toH2Console()).permitAll()
                        // .requestMatchers(getPublicRoutes()).permitAll()
                        .requestMatchers(getPublicRoutes()).permitAll()
                        // .requestMatchers(getPublicMvcRoutes(introspector)).permitAll()
                        // .requestMatchers(h2Console).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        // .failureUrl("/login?error=true")
                        // .defaultSuccessUrl("/")
                        // .loginProcessingUrl("/")
                        .permitAll())
                .rememberMe(Customizer.withDefaults())
                .logout(LogoutConfigurer::permitAll);
        // .logout(logout -> logout
        // .logoutSuccessUrl("/login")
        // .invalidateHttpSession(true)
        // .clearAuthentication(true)
        // .deleteCookies("JSESSIONID")
        // .permitAll());

        return http.build();
    }

    // added CustomUserDetailsService with h2. Don't need this anymore
    // @Bean
    // public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder)
    // {
    // UserDetails user = User.builder()
    // .username("user")
    // .password(passwordEncoder.encode("password"))
    // .roles("USER_U")
    // .build();
    // UserDetails admin = User.builder()
    // .username("admin")
    // .password(passwordEncoder.encode("password"))
    // // .password("password")
    // .roles("ADMIN")
    // .build();
    //
    // return new InMemoryUserDetailsManager(user, admin);
    // }

}
