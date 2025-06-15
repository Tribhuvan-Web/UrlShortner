package com.url.shortner.service.emailreset;

import com.url.shortner.models.User;
import com.url.shortner.repository.UserRepository;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class PasswordResetService {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // Generate password reset token
    public String generatePasswordResetToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    // Validate password reset token
    public String validatePasswordResetToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token)
                    .getPayload().getSubject();
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Password reset token has expired");
        } catch (JwtException | IllegalArgumentException ex) {
            throw new RuntimeException("Invalid password reset token");
        }
    }

    // Initiate password reset process
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = generatePasswordResetToken(user.getEmail());
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

        // Send email with reset link
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    // Complete password reset
    public void resetPassword(String token, String newPassword) {
        String email = validatePasswordResetToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
