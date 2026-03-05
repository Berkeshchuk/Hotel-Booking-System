package com.demo_user_service.data.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.demo_user_service.data.enums.AccountState;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    long id;
    @Column(nullable = false, unique = true, length = 32)
    String login;
    @Column(nullable = false)
    String role;
    @Column(nullable = false)
    String hashPassword;
    @Column(nullable = true, unique = true)
    String email;
    @Column(nullable = true, unique = true)
    String phoneNumber;

    @Column(nullable = false)
    private AccountState accountState = AccountState.ACTIVE;

    int trustLevel;
    int consecutiveCancellations;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime registered;

}
