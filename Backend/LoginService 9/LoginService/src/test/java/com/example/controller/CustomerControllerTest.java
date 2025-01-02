package com.example.controller;

import com.example.model.Customer;
import com.example.model.ResetPasswordRequest;
import com.example.service.CustomerService;
import com.example.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");
        customer.setContactNo("1234567890");
        customer.setName("Test User");
    }

    @Test
    void testLoginCustomer_Success() {
        // Mocking the behavior of the service
        when(customerService.findByEmail("test@example.com")).thenReturn(customer);
        when(customerService.isPasswordValid("password", customer.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(customer.getEmail(), customer.getName(), customer.getContactNo(), customer.getId(), "user"))
            .thenReturn("token");

        ResponseEntity<?> response = customerController.loginCustomer(customer, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof CustomerController.LoginResponse);
        CustomerController.LoginResponse loginResponse = (CustomerController.LoginResponse) response.getBody();
        assertEquals("Login successful", loginResponse.getMessage());
        assertEquals("token", loginResponse.getToken());
    }

    @Test
    void testLoginCustomer_UserNotFound() {
        // Mocking behavior when user is not found
        when(customerService.findByEmail("test@example.com")).thenReturn(null);

        ResponseEntity<?> response = customerController.loginCustomer(customer, null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof java.util.List);
        @SuppressWarnings("unchecked")
        java.util.List<String> errors = (java.util.List<String>) response.getBody();
        assertTrue(errors.contains("User not found"));
    }

    @Test
    void testResetPassword_Success() {
        // Creating a request to reset the password
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("test@example.com");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        // Mocking the customer service and password encoder
        when(customerService.findByEmail("test@example.com")).thenReturn(customer);
        when(passwordEncoder.encode("newPassword")).thenReturn("hashedPassword");

        ResponseEntity<?> response = customerController.resetPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully", response.getBody());
    }

    @Test
    void testResetPassword_Failure() {
        // Creating a request with mismatched passwords
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("test@example.com");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("differentPassword");

        ResponseEntity<?> response = customerController.resetPassword(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof java.util.List);
        @SuppressWarnings("unchecked")
        java.util.List<String> errors = (java.util.List<String>) response.getBody();
        assertTrue(errors.contains("Passwords do not match"));
    }

    @Test
    void testForgotPassword_Success() {
        // Mocking a customer with an email and contact number
        customer.setContactNo("1234567890");
        when(customerService.findByEmailAndContactNo("test@example.com", "1234567890")).thenReturn(customer);

        ResponseEntity<?> response = customerController.forgotPassword(customer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verification successful. Please proceed to reset your password.", response.getBody());
    }

    @Test
    void testForgotPassword_Failure() {
        // Mocking a scenario where no customer is found
        when(customerService.findByEmailAndContactNo("test@example.com", "1234567890")).thenReturn(null);

        ResponseEntity<?> response = customerController.forgotPassword(customer);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof java.util.List);
        @SuppressWarnings("unchecked")
        java.util.List<String> errors = (java.util.List<String>) response.getBody();
        assertTrue(errors.contains("User not found or contact number does not match"));
    }
}
