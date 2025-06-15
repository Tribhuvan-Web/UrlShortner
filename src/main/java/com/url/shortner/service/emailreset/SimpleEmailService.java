package com.url.shortner.service.emailreset;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SimpleEmailService implements EmailService {

    private final JavaMailSender mailSender;

    // Constructor injection is preferred over @Autowired
    public SimpleEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@yourdomain.com");
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, click the following link: " + resetLink +
                    "\n\nIf you didn't request this, please ignore this email." +
                    "\n\nThis link will expire in 24 hours for security reasons.");

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage(), e);
        }
    }
}