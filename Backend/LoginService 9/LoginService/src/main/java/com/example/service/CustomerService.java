package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
  // BCrypt encoder
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // Find customer by email for login
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // Check if the password matches (hashed comparison)
    public Customer findByEmailAndContactNo(String email, String contactNo) {
        return customerRepository.findByEmailAndContactNo(email, contactNo);
    }
    public void save(Customer customer) {
        customerRepository.save(customer);
    }
    // Validate the password (hashed comparison)
    public boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);  // Compare raw and encoded passwords
    }
}
