import API from '../api_access/api';

const CourierService = {
    // Get all couriers
    getAllCouriers: () => {
        return API.get('/couriers');
    },

    // Get courier by ID
    getCourier: (id) => {
        return API.get(`/couriers/${id}`);
    },

    // Create a new courier
    createCourier: (courierData) => {
        return API.post('/couriers', courierData);
    },

    setAvailability: (id,availability) => {
        return API.put(`/couriers/${id}/availability`, availability);
    },

    // Update a courier
    updateCourier: (id, courierData) => {
        return API.put(`/couriers/${id}`, courierData);
    },

    // Delete a courier
    deleteCourier: (id) => {
        return API.delete(`/couriers/${id}`);
    },

    // Get deliveries assigned to courier
    getCourierDeliveries: (courierId) => {
        return API.get(`/couriers/${courierId}/deliveries`);
    },

    // Mark delivery as complete
    completeDelivery: (courierId, deliveryId) => {
        return API.post(`/couriers/${courierId}/deliveries/${deliveryId}/complete`);
    }
};

export default CourierService;
