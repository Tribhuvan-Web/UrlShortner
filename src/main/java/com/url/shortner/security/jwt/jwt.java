package com.url.shortner.security.jwt;

import java.security.Key;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component // => @Component is the spring core annotation that marks a java class as a
           // spring managed component or bean.
public class jwt {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private String jwtExpirations;

    public String getJwtFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }

     private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtExpirations));
    }

    @SuppressWarnings("unused")
    private String getUserNameFromJWTToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    @SuppressWarnings("unused")
    private boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
