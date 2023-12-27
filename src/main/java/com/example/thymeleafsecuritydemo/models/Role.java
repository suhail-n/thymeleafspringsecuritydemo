package com.example.thymeleafsecuritydemo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ToString.Exclude
    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "roles_privileges", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "privilege_id", referencedColumnName = "id") })
    private Collection<Privilege> privileges;

}
