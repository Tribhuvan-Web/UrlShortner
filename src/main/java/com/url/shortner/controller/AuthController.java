package com.url.shortner.controller;

import com.url.shortner.Exception.UserNameAlreadyExists;
import com.url.shortner.Exception.UserNameNotFound;
import com.url.shortner.dtos.LoginRequest;
import com.url.shortner.dtos.RegisterRequest;
import com.url.shortner.models.User;
import com.url.shortner.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "Authentication related APIs")
public class AuthController {

    private UserService userService;

    @PostMapping("/public/login")
    @Operation(summary = "Login user")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(userService.authenticateUser(loginRequest));
        } catch (UserNameNotFound e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("incorrect username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Incorrect username or password");
        }
    }

    @PostMapping("/public/register")
    @Operation(summary = "Register user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        // Controllers for handling the user unique data
        try {
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(registerRequest.getPassword());
            user.setEmail(registerRequest.getEmail());
            user.setRole("ROLE_USER");
            userService.registerUser(user);
            return ResponseEntity.ok("User Registered successfully");
        } catch (UserNameAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists. Please try another username.");
        }
    }

}
