import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import './Register.css';
import CustomerService from '../services/customerService';
import RestaurantService from "../services/restaurantService";
import CourierService from '../services/courierService';
import api from "../api_access/api";

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    accountType: '',
    username: '',
    password: '',
    email: '',
    phoneNumber: '',
    firstName: '',
    lastName: '',
    address: '',
    name: '',
    description: '',
    cuisineType: '',
    businessHours: '',
    deliveryRange: '',
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [isSuccess, setIsSuccess] = useState(false);

  const accountTypes = [
    { value: 'courier', label: 'Courier' },
    { value: 'restaurant', label: 'Restaurant' },
    { value: 'customer', label: 'Customer' }
  ];

  // Enum cuisine types for dropdown
  const cuisineTypes = [
    'HAMBURGER',
    'PIZZA',
    'HEALTHY',
    'ITALIAN',
    'SEAFOOD',
    'DESSERT',
    'DRINKS',
    'TRADITIONAL',
    'VEGAN'
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');
    setIsSuccess(false);

    try {
      let res;
      const finalData = { ...formData };

      // Username check
      await api.post('/auth/check-username', finalData);

      switch (finalData.accountType) {
        case 'customer':
          res = await CustomerService.createCustomer(finalData);
          break;
        case 'restaurant':
          res = await RestaurantService.createRestaurant(finalData);
          break;
        case 'courier':
          res = await CourierService.createCourier(finalData);
          break;
        default:
          throw new Error('Invalid account type');
      }

      setMessage('Registration successful! Redirecting to login...');
      setIsSuccess(true);

      setTimeout(() => {
        navigate('/login');
      }, 3000);

    } catch (err) {
      const msg = err?.response?.data?.message || err?.response?.data || err.message || 'Registration failed. Please try again.';
      setError(msg);
    }
  };

  return (
      <div className="auth-container">
        <div className="auth-header">
          <h1>Pina</h1>
        </div>
        <div className="auth-content">
          {isSuccess ? (
              <div className="auth-form-container">
                <h2>Success!</h2>
                <div className="success-message">{message}</div>
              </div>
          ) : (
              <>
                <div className="account-type-section">
                  <h2>Please Select Your Account Type</h2>
                  <div className="select-wrapper">
                    <select
                        name="accountType"
                        value={formData.accountType}
                        onChange={handleChange}
                        className="account-type-select"
                        required
                    >
                      <option value="" disabled>
                        Select account type
                      </option>
                      {accountTypes.map((type) => (
                          <option key={type.value} value={type.value}>
                            {type.label}
                          </option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className="auth-form-container">
                  <h2>Register Form</h2>
                  {error && <div className="error-message">{error}</div>}
                  <form onSubmit={handleSubmit} className="auth-form">
                    <div className="form-grid">
                      {/* Common Fields */}
                      <div className="form-group">
                        <label>Username</label>
                        <input
                            type="text"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
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
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Email</label>
                        <input
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Phone Number</label>
                        <input
                            type="text"
                            name="phoneNumber"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            required
                        />
                      </div>

                      {/* Conditional Fields */}
                      {formData.accountType === "courier" && (
                          <>
                            <div className="form-group">
                              <label>First Name</label>
                              <input
                                  type="text"
                                  name="firstName"
                                  value={formData.firstName}
                                  onChange={handleChange}
                                  required
                              />
                            </div>
                            <div className="form-group">
                              <label>Last Name</label>
                              <input
                                  type="text"
                                  name="lastName"
                                  value={formData.lastName}
                                  onChange={handleChange}
                                  required
                              />
                            </div>
                            <div className="form-group">
                              <label>Address</label>
                              <input
                                  type="text"
                                  name="address"
                                  value={formData.address}
                                  onChange={handleChange}
                                  required
                              />
                            </div>
                          </>
                      )}

                      {formData.accountType === "customer" && (
                          <>
                            <div className="form-group">
                              <label>First Name</label>
                              <input
                                  type="text"
                                  name="firstName"
                                  value={formData.firstName}
                                  onChange={handleChange}
                                  required
                              />
                            </div>
                            <div className="form-group">
                              <label>Last Name</label>
                              <input
                                  type="text"
                                  name="lastName"
                                  value={formData.lastName}
                                  onChange={handleChange}
                                  required
                              />
                            </div>
                            <div className="form-group">
                              <label>Address</label>
                              <input
                                  type="text"
                                  name="address"
                                  value={formData.address}
                                  onChange={handleChange}
                                  required
                              />
                            </div>
                          </>
                      )}

                      {formData.accountType === "restaurant" && (
                          <>
                            <div className="form-group">
                              <label>Restaurant Name</label>
                              <input
                                  type="text"
                                  name="name"
                                  value={formData.name}
                                  onChange={handleChange}
                                  required
                              />
                            </div>

                            <div className="form-group">
                              <label>Address</label>
                              <input
                                  type="text"
                                  name="address"
                                  value={formData.address}
                                  onChange={handleChange}
                                  required
                              />
                            </div>

                            <div className="form-group">
                              <label>Cuisine Type</label>
                              <select
                                  name="cuisineType"
                                  value={formData.cuisineType}
                                  onChange={handleChange}
                                  required
                              >
                                <option value="" disabled>
                                  Select cuisine type
                                </option>
                                {cuisineTypes.map((type) => (
                                    <option key={type} value={type}>
                                      {type}
                                    </option>
                                ))}
                              </select>
                            </div>

                            <div className="form-group">
                              <label>Business Hours</label>
                              <input
                                  type="text"
                                  name="businessHours"
                                  value={formData.businessHours}
                                  onChange={handleChange}
                                  required
                              />
                            </div>

                            <div className="form-group">
                              <label>Delivery Range (km)</label>
                              <input
                                  type="number"
                                  name="deliveryRange"
                                  value={formData.deliveryRange}
                                  onChange={handleChange}
                                  required
                                  min={0}
                              />
                            </div>

                            {/* Moved Description to the end and made bigger */}
                            <div className="form-group description-group">
                              <label>Description</label>
                              <textarea
                                  name="description"
                                  value={formData.description}
                                  onChange={handleChange}
                                  required
                                  rows={4}
                                  style={{ resize: "vertical" }}
                              />
                            </div>
                          </>
                      )}
                    </div>

                    <button type="submit" className="submit-button">
                      Register
                    </button>
                    <p className="login-link">
                      Already have an account? <Link to="/login">Back to Login</Link>
                    </p>
                  </form>
                </div>
              </>
          )}
        </div>
      </div>
  );

};

export default Register;
