package com.example.thymeleafsecuritydemo.repository;

import com.example.thymeleafsecuritydemo.models.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// By default Spring will only rollback unchecked exceptions but you should rollback for checked exceptions as well.
// @Transactional(rollback=Exception.class)

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Transactional(readOnly = true)
    Optional<UserEntity> findByEmail(String email);

    @Transactional(readOnly = true)
    Optional<UserEntity> findByUsername(String username);
}
