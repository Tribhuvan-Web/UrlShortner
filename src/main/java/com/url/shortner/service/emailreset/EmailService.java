package com.url.shortner.service.emailreset;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetLink);
}
