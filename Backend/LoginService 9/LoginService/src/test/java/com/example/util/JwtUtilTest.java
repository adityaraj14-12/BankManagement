package com.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private final String secretKey = "mySecretKey"; // Use the secret key from your JwtUtil class

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateToken() {
        // Assuming generateToken now accepts additional params: name, contactNo
        String token = jwtUtil.generateToken("test@example.com", "Test User", "1234567890", 1L, "user");

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void testExtractUsername() {
        // Generate a token with email
        String token = jwtUtil.generateToken("test@example.com", "Test User", "1234567890", 1L, "user");

        // Extract username (email in this case)
        String username = jwtUtil.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void testExtractRole() {
        // Generate a token with role
        String token = jwtUtil.generateToken("test@example.com", "Test User", "1234567890", 1L, "admin");

        // Extract role from the token
        String role = jwtUtil.extractRole(token);

        assertEquals("admin", role);
    }

    @Test
    void testValidateToken_Valid() {
        // Generate a token for a valid user
        String token = jwtUtil.generateToken("test@example.com", "Test User", "1234567890", 1L, "user");

        // Validate the token
        boolean isValid = jwtUtil.validateToken(token, "test@example.com");

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_Invalid() {
        // Generate a token for a valid user
        String token = jwtUtil.generateToken("test@example.com", "Test User", "1234567890", 1L, "user");

        // Validate the token with incorrect username
        boolean isValid = jwtUtil.validateToken(token, "wrong@example.com");

        assertFalse(isValid);
    }

  }
