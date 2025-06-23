package com.url.shortner.service.OAuth;

import com.url.shortner.models.User;
import com.url.shortner.repository.UserRepository;
import com.url.shortner.security.jwt.JwtUtils;
import com.url.shortner.service.userService.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private JwtUtils tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Required for password generation

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        logger.info("OAuth2 authentication successful");

        try {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oauthToken.getPrincipal();

            // Extract user information
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String providerId = oAuth2User.getAttribute("sub"); // Google's unique ID

            // 1. Check if user exists by provider ID (Google ID)
            Optional<User> existingUser = userRepository.findByProviderAndProviderId(User.AuthProvider.GOOGLE, providerId);

            User user;
            if (existingUser.isPresent()) {
                // 2a. Existing OAuth user: Update profile if needed
                user = existingUser.get();
                logger.info("Existing OAuth user found: {}", email);
            } else {
                // 2b. Check if email exists (local user)
                Optional<User> localUser = userRepository.findByEmail(email);

                if (localUser.isPresent()) {
                    // 3a. Existing local user: Convert to OAuth
                    user = localUser.get();
                    user.setProvider(User.AuthProvider.GOOGLE);
                    user.setProviderId(providerId);
                    logger.info("Converting local user to OAuth: {}", email);
                } else {
                    // 3b. New user: Create with generated password
                    user = new User();
                    user.setEmail(email);
                    user.setUsername(email); // Use email as username
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Random secure password
                    user.setRole("ROLE_USER");
                    user.setProvider(User.AuthProvider.GOOGLE);
                    user.setProviderId(providerId);
                    logger.info("Creating new OAuth user: {}", email);
                }
                user = userRepository.save(user);
            }

            // 4. Generate JWT from UserDetails
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            String token = tokenProvider.generateToken(userDetails);

            // 5. Redirect to frontend
            String redirectUrl = frontendUrl + "/login?token=" + token;
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            logger.error("OAuth2 authentication failed", e);
            response.sendRedirect(frontendUrl + "/login?error=authentication_failed");
        }
    }
}