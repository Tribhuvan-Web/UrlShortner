package com.url.shortner.controller;

import com.url.shortner.Exception.UserNameAlreadyExists;
import com.url.shortner.Exception.UserNameNotFound;
import com.url.shortner.dtos.LoginRequest;
import com.url.shortner.dtos.RegisterRequest;
import com.url.shortner.models.User;
import com.url.shortner.security.jwt.JwtUtils;
import com.url.shortner.service.emailreset.PasswordResetService;
import com.url.shortner.service.userService.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "Authentication related APIs")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            // Add basic email validation
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            passwordResetService.initiatePasswordReset(email);
            return ResponseEntity.ok("Password reset email sent");
        } catch (RuntimeException e) {
            // Don't reveal if user exists or not for security reasons
            return ResponseEntity.badRequest().body("Please try again with the correct email");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        try {
            // Add basic password validation
            if (newPassword == null || newPassword.trim().length() < 6) {
                return ResponseEntity.badRequest().body("Password must be at least 6 characters long");
            }

            passwordResetService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password has been reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/public/login")
    @Operation(summary = "Login user")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(userService.authenticateUser(loginRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }


    @PostMapping("/public/register")
    @Operation(summary = "Register user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(registerRequest.getPassword());
            user.setEmail(registerRequest.getEmail());
            user.setRole("ROLE_USER");
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (UserNameAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username/Email already exists.");
        }
    }

    @GetMapping("/username")
    public ResponseEntity<?> getUsername(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Invalid authorization header");
            }

            String token = authHeader.substring(7);
            String username = jwtUtils.getUserNameFromJwtToken(token);
            return ResponseEntity.ok(username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}