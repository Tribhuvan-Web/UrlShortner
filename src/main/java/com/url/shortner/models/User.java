package com.url.shortner.models;

import jakarta.persistence.*;
import lombok.Data;

import java.security.AuthProvider;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;
    private String role = "ROLE_USER";

    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.LOCAL;

    private String providerId;

    public enum AuthProvider {
        LOCAL, GOOGLE
    }
}