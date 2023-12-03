package com.example.thymeleafsecuritydemo.repository;

import com.example.thymeleafsecuritydemo.models.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Transactional(readOnly = true)
    Optional<Role> findByName(String name);
}