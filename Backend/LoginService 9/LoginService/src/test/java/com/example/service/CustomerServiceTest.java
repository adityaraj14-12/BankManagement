package com.example.service;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");
    }

    @Test
    void testFindByEmail() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(customer);

        Customer result = customerService.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testIsPasswordValid_Success() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password");
        when(customerRepository.findByEmail("test@example.com")).thenReturn(customer);

        boolean isValid = customerService.isPasswordValid("password", encodedPassword);

        assertTrue(isValid);
    }

    @Test
    void testIsPasswordValid_Failure() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("wrongPassword");

        boolean isValid = customerService.isPasswordValid("password", encodedPassword);

        assertFalse(isValid);
    }

    @Test
    void testFindByEmailAndContactNo() {
        when(customerRepository.findByEmailAndContactNo("test@example.com", "1234567890")).thenReturn(customer);

        Customer result = customerService.findByEmailAndContactNo("test@example.com", "1234567890");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }
}
