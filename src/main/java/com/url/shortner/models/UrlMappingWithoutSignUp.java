package com.url.shortner.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UrlMappingWithoutSignUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user; // Nullable for anonymous URLs
}