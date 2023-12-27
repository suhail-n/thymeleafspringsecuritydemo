package com.example.thymeleafsecuritydemo.repository;

import com.example.thymeleafsecuritydemo.models.Activation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivationRepository extends JpaRepository<Activation, Long> {

    @Transactional(readOnly = true)
    Optional<Activation> findByToken(String token);
}
