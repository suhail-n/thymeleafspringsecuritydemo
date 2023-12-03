package com.example.thymeleafsecuritydemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.thymeleafsecuritydemo.models.Privilege;

import java.util.Optional;

@Transactional(readOnly = true)
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByName(String name);
}
