package com.url.shortner.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitingFilter implements Filter {

    // Configure the Bucket (e.g., 10 requests per minute)
    private final Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1))))
            .build();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Check if a token is available
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(429);
            response.getWriter().write("Too many requests - Please wait for some time");
        }
    }
}