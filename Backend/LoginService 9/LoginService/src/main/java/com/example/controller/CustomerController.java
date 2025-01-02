package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Customer;
import com.example.model.ResetPasswordRequest;
import com.example.service.CustomerService;
import com.example.util.JwtUtil; // Import JWT utility class
@RestController
@RequestMapping("/api/auth/login")  // Allow CORS from your frontend React app
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder


    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "admin123";  // You can store this hashed for security

    // Structured response for login
    public static class LoginResponse {
        private String message;
        private String token;

        // Constructor
        public LoginResponse(String message, String token) {
            this.message = message;
            this.token = token;
        }

        // Getters
        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }
    }

    @PostMapping("/logi")
    public ResponseEntity<?> loginCustomer(@RequestBody Customer customer, BindingResult result) {
        List<String> errors = new ArrayList<>();

        // Check if email and password are provided
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            errors.add("Email is required");
        }

        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            errors.add("Password is required");
        }

        // If there are validation errors, return them
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        if (customer.getEmail().equals(ADMIN_EMAIL) && customer.getPassword().equals(ADMIN_PASSWORD)) {
            // Generate JWT token for admin
            String token = jwtUtil.generateToken(customer.getEmail(),null,null, -1L, "admin"); // Admin doesn't need userId
            return ResponseEntity.ok(new LoginResponse("Admin Login successful", token));
        }

        // Check if the customer exists based on the provided email
        Customer foundCustomer = customerService.findByEmail(customer.getEmail());
        if (foundCustomer == null) {
            errors.add("User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);  // User not found
        }

        // Validate the password
        if (!customerService.isPasswordValid(customer.getPassword(), foundCustomer.getPassword())) {
            errors.add("Invalid password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);  // Invalid password
        }

        // Generate JWT token for the normal user
        String token = jwtUtil.generateToken(foundCustomer.getEmail(),foundCustomer.getName(),foundCustomer.getContactNo() ,foundCustomer.getId(), "user");

        // Return structured response with the token
        LoginResponse response = new LoginResponse("Login successful", token);
        return ResponseEntity.ok(response);  // Return success response with token
    }    
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        List<String> errors = new ArrayList<>();

        // Validation checks for new password
        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            errors.add("New password is required");
        }

        if (request.getConfirmPassword() == null || !request.getNewPassword().equals(request.getConfirmPassword())) {
            errors.add("Passwords do not match");
        }

        // If there are validation errors, return them
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // Find the customer by email
        Customer customer = customerService.findByEmail(request.getEmail());
        if (customer == null) {
            errors.add("User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
        }

        // Hash the new password before saving it to the database
        String hashedPassword = passwordEncoder.encode(request.getNewPassword());
        customer.setPassword(hashedPassword);

        // Save the customer with the new hashed password
        customerService.save(customer);

        return ResponseEntity.ok("Password updated successfully");
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Customer customer) {
        List<String> errors = new ArrayList<>();

        // Validate email and phone number
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            errors.add("Email is required");
        }

        if (customer.getContactNo() == null || customer.getContactNo().isEmpty()) {
            errors.add("Contact number is required");
        }

        // If there are validation errors, return them
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // Check if customer exists with provided email and phone number
        Customer foundCustomer = customerService.findByEmailAndContactNo(customer.getEmail(), customer.getContactNo());
        if (foundCustomer == null) {
            errors.add("User not found or contact number does not match");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
        }

        // Send email or SMS to verify identity (Here you can integrate an actual service)
        // For now, we'll assume this is successful.

        // Return a success message (or you could send a reset password link to the user's email)
        return ResponseEntity.ok("Verification successful. Please proceed to reset your password.");
    }

    
    
}
