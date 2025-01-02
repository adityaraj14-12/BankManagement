// src/components/Dashboard.js

import React, { useState, useEffect } from 'react';
import { Container, Typography, Box, Button, Grid, Paper } from '@mui/material';
import { Link } from 'react-router-dom';
import './Dashboard.css'; // Create a separate CSS file for styling

const Dashboard = () => {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);

  // Fetch user dashboard info from backend
  useEffect(() => {
    fetch('http://localhost:8080/api/dashboard', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${sessionStorage.getItem('token')}`, // Pass the token for authentication
      }
    })
    .then((response) => response.json())
    .then((data) => setUserInfo(data))
    .catch((error) => setError('Error fetching dashboard data.'));
  }, []);

  if (error) {
    return <div>{error}</div>;
  }

  if (!userInfo) {
    return <div>Loading...</div>;
  }

  return (
    <Container sx={{ marginTop: '50px' }}>
      <Typography variant="h3" gutterBottom className="dashboard-heading">
        Welcome, {userInfo.username}
      </Typography>

      <Box sx={{ marginBottom: '30px' }}>
        <Typography variant="h5" className="dashboard-subheading">
          Account Details
        </Typography>
        <Paper sx={{ padding: '20px', background: '#f5f5f5' }}>
          <Typography variant="h6">Account Name: {userInfo.accountName}</Typography>
          <Typography variant="h6">Balance: ${userInfo.balance.toFixed(2)}</Typography>
        </Paper>
      </Box>

      <Grid container spacing={2}>
        <Grid item xs={12} sm={6} md={4}>
          <Link to="/transactions">
            <Button fullWidth variant="contained" className="dashboard-btn">
              View Transactions
            </Button>
          </Link>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Link to="/transfer">
            <Button fullWidth variant="contained" className="dashboard-btn">
              Transfer Money
            </Button>
          </Link>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Link to="/settings">
            <Button fullWidth variant="contained" className="dashboard-btn">
              Account Settings
            </Button>
          </Link>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Dashboard;
