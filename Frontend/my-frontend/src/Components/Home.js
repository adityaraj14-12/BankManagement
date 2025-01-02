import React from 'react';
import { Button, Container, Typography, Box } from '@mui/material';
import { Link } from 'react-router-dom';
import './Home.css'; // Import the custom CSS file

const Home = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundImage: 'url(https://images.pexels.com/photos/3833052/pexels-photo-3833052.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1)', // Replace with your background image URL
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        color: '#fff',
        textAlign: 'center',
        boxShadow: 'inset 0px 0px 150px rgba(0, 0, 0, 0.5)',
      }}
    >
      <Container maxWidth="sm" sx={{ position: 'relative', zIndex: 2 }}>
        <Typography variant="h2" className="hero-title">
          Welcome to Our Banking System
        </Typography>
        <Typography variant="h5" sx={{ marginBottom: 5 }}>
          Secure, Fast, and Reliable banking services to help you manage your finances.
        </Typography>
        
        <Box sx={{ display: 'flex', justifyContent: 'center', gap: 3 }}>
          <Link to="/login">
            <Button
              variant="contained"
              className="login-btn"
            >
              Login
            </Button>
          </Link>

          <Link to="/register">
            <Button
              variant="outlined"
              className="register-btn"
            >
              Register
            </Button>
          </Link>
        </Box>
      </Container>
    </Box>
  );
};

export default Home;
