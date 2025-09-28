import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Login.css';
import api from '../api_access/api';

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await api.post('/auth/login', formData);
      const {id, accountType, token } = response.data;

      localStorage.setItem('userId', id);
      localStorage.setItem('accountType', accountType);
      localStorage.setItem('token', token);

      switch (accountType) {
        case 'customer':
          navigate('/customer');
          break;
        case 'restaurant':
          navigate('/restaurant');
          break;
        case 'courier':
          navigate('/courier');
          break;
        case 'admin':
          navigate('/admin');
          break;
        default:
          navigate('/');
      }
    }
    catch (err) {
      setError(err.response?.data?.message || err.response?.data?.error ||err.response?.data|| 'Login failed. Please try again.');

    }
  };

  return (
      <div className="auth-container">
        <div className="auth-header">
          <h1>Pina</h1>
        </div>
        <div className="auth-content">
          <div className="welcome-section">
            <h2>Hi!</h2>
            <h3>Welcome To Pina</h3>
            <p>Craving something delicious?</p>
            <p>Pina brings your favorite restaurants straight to your doorstep.</p>
            <p>Browse through a variety of cuisines, place your order in seconds, and enjoy fast, reliable delivery.</p>
          </div>
          <div className="auth-form-container">
            <h2>Login</h2>
            {error && <div className="error-message">{error}</div>}
            <form onSubmit={handleSubmit} className="auth-form">
              <div className="form-group">
                <label>Username</label>
                <input
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    className="input-field"
                    required
                />
              </div>
              <div className="form-group">
                <label>Password</label>
                <input
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    className="input-field"
                    required
                />
              </div>
              <button type="submit" className="submit-button">
                Submit
              </button>
              <p className="register-link">
                Don't have an account? <Link to="/register">Register</Link>
              </p>
            </form>
          </div>
        </div>
      </div>
  );
};

export default Login;
