import API from '../api_access/api';

const OrderService = {
    // Get all orders
    getAllOrders: () => {
        return API.get('/orders');
    },

    // Get order by ID
    getOrder: (id) => {
        return API.get(`/orders/${id}`);
    },

    // Get orders by customer
    getOrdersByCustomer: (customerId) => {
        return API.get(`/orders/customer/${customerId}`);
    },

    // Get orders by restaurant
    getOrdersByRestaurant: (restaurantId) => {
        return API.get(`/orders/restaurant/${restaurantId}`);
    },

    // Get orders by courier
    getOrdersByCourier: (courierId) => {
        return API.get(`/orders/courier/${courierId}`);
    },

    // Get orders by status
    getOrdersByStatus: (status) => {
        return API.get(`/orders/status/${status}`);
    },

    // Get orders by time range
    getOrdersByTimeRange: (start, end) => {
        return API.get(`/orders/timerange?start=${start}&end=${end}`);
    },

    // Place a new order
    placeOrder: (customerId, restaurantId, deliveryAddress, orderItems) => {
        return API.post(`/orders?customerId=${customerId}&restaurantId=${restaurantId}&deliveryAddress=${deliveryAddress}`, orderItems);
    },

    // Update order status
    updateOrderStatus: (id, status) => {
        return API.put(`/orders/${id}/status?status=${status}`);
    },

    setOrderNote: (id, note) => {
        return API.put(`/orders/note?id=${id}&note=${note}`);
    },

    // Assign courier to order
    assignCourier: (orderId, courierId) => {
        return API.put(`/orders/${orderId}/assign-courier/${courierId}`);
    },
    unassignCourier: (orderId, courierId) => {
        return API.delete(`/orders/${orderId}/unassign-courier`);
    },

    // Get order items
    getOrderItems: (id) => {
        return API.get(`/orders/${id}/items`);
    }
};

export default OrderService;