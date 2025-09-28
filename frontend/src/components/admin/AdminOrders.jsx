import React, { useEffect, useState } from 'react';
import API from '../api_access/api';
import './AdminCouriers.css';

const AdminOrders = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const res = await API.get('/admin/orders');
        setOrders(res.data);
      } catch (err) {
        console.error('Order fetch error:', err);
      }
    };

    fetchOrders();
  }, []);

  return (
    <div className="courier-table-wrapper">
      <h2>Orders</h2>
      <table className="courier-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Status</th>
            <th>Total</th>
            <th>Payment</th>
            <th>Payment Status</th>
            <th>Order Time</th>
            <th>Delivery Time</th>
            <th>Delivery Address</th>
            <th>Instructions</th>
            <th>Customer ID</th>
            <th>Restaurant ID</th>
            <th>Courier ID</th>
          </tr>
        </thead>
        <tbody>
          {orders.map(order => (
            <tr key={order.id}>
              <td>{order.id}</td>
              <td>{order.orderStatus}</td>
              <td>{order.totalAmount} ₺</td>
              <td>{order.paymentMethod}</td>
              <td>{order.paymentStatus}</td>
              <td>{order.orderTime}</td>
              <td>{order.deliveryTime}</td>
              <td>{order.deliveryAddress}</td>
              <td>{order.specialInstructions}</td>
              <td>{order.customerId}</td>
              <td>{order.restaurantId}</td>
              <td>{order.courierId}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminOrders;