package com.example.thymeleafsecuritydemo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "activation")
public class Activation {
    // Expiration time 10 minutes
    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    private Date expirationTime;

    @ToString.Exclude
    @OneToOne(mappedBy = "activation", fetch = FetchType.EAGER)
    private UserEntity user;

    @PrePersist
    void preInsert() {
        this.setExpirationTime(calculateExpirationDate());
    }

    @PreUpdate
    void preUpdate()  {
        this.setExpirationTime(calculateExpirationDate());
    }

    private Date calculateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, Activation.EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }

    public boolean isActive() {
        return this.getExpirationTime().after(
                Calendar
                        .getInstance()
                        .getTime());
    }

}
