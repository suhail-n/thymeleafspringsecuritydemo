package com.example.thymeleafsecuritydemo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "roles_privileges", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "privilege_id", referencedColumnName = "id") })
    private Collection<Privilege> privileges;

}
