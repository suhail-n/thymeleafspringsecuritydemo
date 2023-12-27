package com.example.thymeleafsecuritydemo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ToString.Exclude
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
}
