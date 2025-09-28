import API from '../api_access/api';

const RestaurantService = {
    // Get all restaurants
    getAllRestaurants: async () => {
        const response = await API.get('/restaurants');
        return response.data;
    },

    // Get approved restaurants
    getApprovedRestaurants: () => {
        return API.get('/restaurants/approved');
    },

    // Get restaurants by cuisine type
    getRestaurantsByCuisine: (cuisineType) => {
        return API.get(`/restaurants/cuisine/${cuisineType}`);
    },

    // Search restaurants by name
    searchRestaurants: (name) => {
        return API.get(`/restaurants/search?name=${name}`);
    },

    // Get restaurant by ID
    getRestaurant: (id) => {
        return API.get(`/restaurants/${id}`);
    },

    // Create a new restaurant
    createRestaurant: (restaurantData) => {
        return API.post('/restaurants', restaurantData);
    },

    // Update a restaurant
    updateRestaurant: (id, restaurantData) => {
        return API.put(`/restaurants/${id}`, restaurantData);
    },

    // Delete a restaurant
    deleteRestaurant: (id) => {
        return API.delete(`/restaurants/${id}`);
    },

    // Approve a restaurant
    approveRestaurant: (id) => {
        return API.put(`/restaurants/${id}/approve`);
    },

    // Reject a restaurant
    rejectRestaurant: (id) => {
        return API.put(`/restaurants/${id}/reject`);
    },

    // Get restaurant menu
    getRestaurantMenu: (id) => {
        return API.get(`/restaurants/${id}/menu`);
    },

    // Add menu item
    addMenuItem: (restaurantId, menuItemData) => {
        return API.post(`/restaurants/${restaurantId}/menu`, menuItemData);
    },

    // Update menu item
    updateMenuItem: (restaurantId, itemId, menuItemData) => {
        return API.put(`/restaurants/${restaurantId}/menu/${itemId}`, menuItemData);
    },

    // Delete menu item
    deleteMenuItem: (restaurantId, itemId) => {
        return API.delete(`/restaurants/${restaurantId}/menu/${itemId}`);
    },

    // Get registered couriers
    getRegisteredCouriers: (restaurantId) => {
        return API.get(`/restaurants/${restaurantId}/couriers`);
    },

    // Register courier
    registerCourier: (restaurantId, courierId) => {
        return API.post(`/restaurants/${restaurantId}/couriers/${courierId}`);
    },

    // Unregister courier
    unregisterCourier: (restaurantId, courierId) => {
        return API.delete(`/restaurants/${restaurantId}/couriers/${courierId}`);
    }
};

export default RestaurantService;