package com.example.thymeleafsecuritydemo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(length = 80)
    private String password;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private Collection<Role> roles;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "activation_id", referencedColumnName = "id")
    private Activation activation;

    @Builder.Default
    private boolean enabled = false;
}
