import API from '../api_access/api';

const CustomerService = {
    // Get all customers
    getAllCustomers: () => {
        return API.get('/customers');
    },

    // Get customer by ID
    getCustomer: (id) => {
        return API.get(`/customers/${id}`);
    },

    // Create a new customer
    createCustomer: (customerData) => {
        return API.post('/customers', customerData);
    },

    // Update a customer
    updateCustomer: (id, customerData) => {
        return API.put(`/customers/${id}`, customerData);
    },

    // Delete a customer
    deleteCustomer: (id) => {
        return API.delete(`/customers/${id}`);
    },

    // Get customer's favorite restaurants
    getFavoriteRestaurants: (id) => {
        return API.get(`/customers/${id}/favorite-restaurants`);
    },

    // Add a restaurant to favorites
    addFavoriteRestaurant: (customerId, restaurantId) => {
        return API.post(`/customers/${customerId}/favorite-restaurants/${restaurantId}`);
    },

    // Remove a restaurant from favorites
    removeFavoriteRestaurant: (customerId, restaurantId) => {
        return API.delete(`/customers/${customerId}/favorite-restaurants/${restaurantId}`);
    }
};

export default CustomerService;
