// DashboardController.java

package com.example.bankmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    // Endpoint to fetch user's dashboard info
    @GetMapping
    public DashboardInfo getDashboardInfo(Authentication authentication) {
        // Here we simulate fetching the user info from the database
        String username = authentication.getName(); // Get the logged-in user's email/username

        // Simulate user data (In real app, you should fetch from the database)
        return new DashboardInfo(username, "Bank Account", 5000.75);
    }
}
