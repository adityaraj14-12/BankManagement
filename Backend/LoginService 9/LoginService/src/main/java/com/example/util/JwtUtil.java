package com.example.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "dGhpcyBpcyBhIHZlcnkgc3BlY2lhbCBzZWNyZXQga2V5IHRoYXQgd2lsbCBiZSB1c2VkIGZvciBKU1Qgc2lnbmVkIHB1cnBvc2Vz";
    private SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Method to generate a token
    public String generateToken(String username,String name,String contact, Long userId, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("name", name)   // Adding the 'name' as a custom claim
                .claim("contact", contact) // The subject (email or username)
                .claim("userId", userId) // Custom claim for user ID
                .claim("role",role) // Custom claim for role (admin/user)
                .setIssuedAt(new Date()) // Set issue date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, secretKey) // Signing algorithm and secret key
                .compact(); // Build the JWT token
    }

    // Method to extract the username from the token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Extract the subject (email or username)
    }

    // Method to extract the user role from the token
    public String extractRole(String token) {
        return (String) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role"); // Extract the role claim
    }

    // Method to validate the token (check if the token is still valid)
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Method to check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Method to extract the expiration date from the token
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

	
}