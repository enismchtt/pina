import React, { useState , useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './CustomerHome.css';
import axios from 'axios';

import CustomerService from '../services/customerService';
import RestaurantService from '../services/restaurantService';
import OrderService from './CustomerTrackOrder';

const CustomerHome = () => {
  const navigate = useNavigate();
  const customerId = localStorage.getItem("userId");

  const [searchQuery, setSearchQuery] = useState('');
  const [customer, setCustomer] = useState({});
  const [restaurants , setRestaurants] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [favoriteRestaurants, setFavoriteRestaurants] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/api/restaurants")
      .then(response => setRestaurants(response.data))
      .catch(error => console.error("Error fetching restaurants:", error));
  }, []);

  useEffect(() => {
    CustomerService.getCustomer(customerId)
      .then(response => setCustomer(response.data))
      .catch(error => console.error("Failed to fetch customer:", error));
  }, [customerId]);

  useEffect(() => {
    RestaurantService.searchRestaurants(searchQuery)
      .then(response => setRestaurants(response.data))
      .catch(error => console.error("Error searching restaurants:", error));
  }, [searchQuery]);

  useEffect(() => {
    axios.get(`http://localhost:8080/api/customers/${customerId}/favorite-restaurants`)
      .then(response => setFavoriteRestaurants(response.data))
      .catch(error => console.error("Error fetching favorites:", error));
  }, [customerId]);

  const toggleFavorite = (restaurantId) => {
    const isFavorite = favoriteRestaurants.some(f => f.id === restaurantId);
    const url = `http://localhost:8080/api/customers/${customerId}/favorite-restaurants/${restaurantId}`;
    const restaurant = restaurants.find(r => r.id === restaurantId);

    if (isFavorite) {
      axios.delete(url)
        .then(() => setFavoriteRestaurants(prev => prev.filter(f => f.id !== restaurantId)))
        .catch(err => console.error("Failed to remove favorite:", err));
    } else {
      axios.post(url)
        .then(() => setFavoriteRestaurants(prev => [...prev, restaurant]))
        .catch(err => console.error("Failed to add favorite:", err));
    }
  };

  const goToPlaceOrder = () => navigate('/customer/place-order');
  const handleTrackOrderClick = () => {
    localStorage.setItem("customerid",customerId)
    navigate('/customer/track-order')};

  const handleRestaurantClick = (restaurantId) => {
    localStorage.setItem("customerid", customerId);
    localStorage.setItem("restaurantid", restaurantId);
    navigate(`/customer/restaurant/${restaurantId}`);
  };

  const handleNavigateToProfile = () => {
    localStorage.setItem('customerid', customerId);
    navigate('/customer/manage-profile');
  };

  const uniqueCategories = Array.from(new Set(restaurants.map(r => r.cuisineType))).filter(Boolean);

  const filteredRestaurants = selectedCategory === 'all'
    ? restaurants
    : selectedCategory === 'favorites'
      ? favoriteRestaurants
      : restaurants.filter(r => r.cuisineType === selectedCategory);

  return (
    <div className="customer-home">
      <header className="header">
        <div className="header-left">
          <h1>Pina</h1>
          <div className="user-info" onClick={handleNavigateToProfile} style={{ cursor: 'pointer' }} >
            <span className="username">{customer.firstName}</span>
          </div>
        </div>
        <div className="header-center">
          <div className="search-bar">
            <input 
              type="text" 
              placeholder="What would you like to have today ..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="search-input"
            />
            <span>🔍</span>
          </div>
        </div>
        <div className="header-right">
          <div className="order-now-section" onClick={handleTrackOrderClick}>
            <button>Track Order</button>
          </div>
          <div>
            <button className="cart-button" onClick={goToPlaceOrder}>
              <span>🛒</span>
            </button>
          </div>
        </div>
      </header>

      <div className="category-bar">
        <button
          className={`category-btn ${selectedCategory === 'all' ? 'active' : ''}`}
          onClick={() => setSelectedCategory('all')}
        >
          All
        </button>
        <button
          className={`category-btn ${selectedCategory === 'favorites' ? 'active' : ''}`}
          onClick={() => setSelectedCategory('favorites')}
        >
          ❤️ Favorites
        </button>
        {uniqueCategories.map((cat) => (
          <button
            key={cat}
            className={`category-btn ${selectedCategory === cat ? 'active' : ''}`}
            onClick={() => setSelectedCategory(cat)}
          >
            {cat.charAt(0).toUpperCase() + cat.slice(1).toLowerCase()}
          </button>
        ))}
      </div>

      <main className="main-content">
        <h2>Restaurants</h2>
        <hr/>
        <section className="restaurants-grid">
          {filteredRestaurants.map((restaurant) => (
            <div 
              key={restaurant.id} 
              className="restaurant-card"
              onClick={() => handleRestaurantClick(restaurant.id)}
              style={{ position: 'relative', cursor: 'pointer' }}
            >
              <div
                className="heart-icon"
                onClick={(e) => {
                  e.stopPropagation();
                  toggleFavorite(restaurant.id);
                }}
                style={{
                  position: 'absolute',
                  top: '10px',
                  right: '10px',
                  fontSize: '22px',
                  color: favoriteRestaurants.some(f => f.id === restaurant.id) ? 'red' : 'gray',
                  zIndex: 10
                }}
              >
                {favoriteRestaurants.some(f => f.id === restaurant.id) ? '❤️' : '🤍'}
              </div>

              <div className="meal-section-tag">{restaurant.cuisineType}</div>
              <div className="restaurant-image">
                {restaurant.imageUrl ? (
                  <img
                    src={restaurant.imageUrl}
                    alt={restaurant.name}
                    style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                  />
                ) : (
                  <div className="placeholder-image">No Image</div>
                )}
              </div>
              <div className="restaurant-info">
                <div className="restaurant-name-heart">
                  <h3>{restaurant.name}</h3>
                </div>
                <div className="restaurant-rating">
                  Rating: {restaurant.rating} / 5
                </div>
              </div>
            </div>
          ))}
        </section>
      </main>
    </div>
  );
};

export default CustomerHome;
