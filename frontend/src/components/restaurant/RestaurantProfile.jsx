import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './RestaurantProfile.css';
import API from "../api_access/api";
import RestaurantService from "../services/restaurantService";

const RestaurantProfile = () => {
  const navigate = useNavigate();
  const restaurantId = localStorage.getItem('userId');
  const [newPassword, setNewPassword] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [restaurant, setRestaurant] = useState({
    name: '',
    address: '',
    phone: '',
    imageUrl: '',
    description: '',
    businessHours: '',
    cuisineType: '',
  });

  useEffect(() => {
    RestaurantService.getRestaurant(restaurantId)
      .then(res => setRestaurant(res.data))
      .catch(err => console.error('Failed to fetch restaurant data', err));
  }, [restaurantId]);

  const handleChange = (e) => {
    setRestaurant({
      ...restaurant,
      [e.target.name]: e.target.value
    });
  };

  const handleSave = async () => {
    try {
      const auth_json = {
        "username": restaurant.username,
        "password": oldPassword
      };
      await API.post('/auth/check-password', auth_json);

      setRestaurant({
        ...restaurant,
        "password": oldPassword
      });

      await RestaurantService.updateRestaurant(restaurantId, restaurant);

      const finalNewPassword = newPassword === "" ? oldPassword : newPassword;

      const chng_password_json = {
        "username": restaurant.username,
        "newPassword": finalNewPassword,
      };

      await API.post('/auth/change-password', chng_password_json);

      alert("Restaurant updated successfully");
    } catch (err) {
      console.error("Failed to update restaurant", err);
      alert("Failed to update restaurant");
    }
  };
  return (
    <div className="restaurant-profile">
      <h2>Manage Profile</h2>
      <form>
        <div className="form-group">
          <label>Username:</label>
          <input name="username" value={restaurant.username} readOnly/>
        </div>
        <div className="form-group">
          <label>Restaurant Name</label>
          <input name="name" value={restaurant.name} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Address</label>
          <input name="address" value={restaurant.address} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Phone</label>
          <input name="phone" value={restaurant.phone} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Business Hours</label>
          <input type="text" name="closingTime" value={restaurant.businessHours} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Delivery Range (km)</label>
          <input type="number" name="deliveryRange" value={restaurant.deliveryRange} onChange={handleChange} required min={0}
          />
        </div>
        <div className="form-group">
          <label>Description</label>
          <textarea name="description" value={restaurant.description} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Image URL</label>
          <input name="imageUrl" value={restaurant.imageUrl} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Cuisine Type</label>
          <select className="select-large" name="cuisineType" value={restaurant.cuisineType} onChange={handleChange}>
            <option value="">Select Type</option>
            <option value="HAMBURGER">Hamburger</option>
            <option value="PIZZA">Pizza</option>
            <option value="PASTA">Pasta</option>
            <option value="VEGAN">Vegan</option>
            <option value="DESSERT">Dessert</option>
            <option value="TRADITIONAL">Traditional</option>
            <option value="SEAFOOD">Seafood</option>
          </select>
        </div>
        <div className="form-group">
          <label>New Password:</label>
          <input name="newPassword" type="password" value={newPassword  } onChange={(e) => setNewPassword(e.target.value) } />
        </div>
        <div className="form-group">
          <label>Confirm Current Password:</label>
          <input name="oldPassword" type="password" value={oldPassword} onChange={e => setOldPassword(e.target.value)} />
        </div>
        <div className="restaurant-profile-buttons">
          <button type="button" onClick={handleSave}>Save</button>
          <button type="button" onClick={() => navigate('/restaurant')}>Go to Home Page</button>
        </div>
      </form>
    </div>
  );
};

export default RestaurantProfile;