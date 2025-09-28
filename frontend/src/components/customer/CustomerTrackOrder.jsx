import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import OrderService from '../services/orderService';
import styles from './CustomerTrackOrder.module.css';
import CustomerService from '../services/customerService';

const CustomerTrackOrder = () => {
    const [orders, setOrders] = useState([]);
    const [customer, setCustomer] = useState({});

    useEffect(() => {
    const customerId = localStorage.getItem('customerid'); // e.g., just the ID stored

    if (!customerId) {
      console.warn('No customerId in localStorage');
      return;
    }

    // Step 1: Fetch full customer
    CustomerService.getCustomer(customerId)
      .then((res) => {
        const customerData = res.data;
        setCustomer(customerData);

        // Step 2: Fetch orders using the customer ID
        return OrderService.getOrdersByCustomer(customerData.id);
      })
      .then((res) => {
        setOrders(res.data);
      })
      .catch((error) => {
        console.error('Error fetching customer or orders:', error);
      });
  }, []);

    return (
        <div className={styles.trackOrder}>
            <header className={styles.header}>
                <div className={styles.headerLeft}>
                    <h1>Pina</h1>
                    {/* Account info container with rounded box */}
                    <div className={styles.accountInfoContainer}>
                        <span className={styles.firstname}>{customer.firstName}</span>
                        <span className={styles.accountType}>{customer.accountType}</span>
                    </div>
                </div>
                <div className={styles.headerCenter}>
                    <h3>Track Order</h3>
                </div>
            </header>

            {/* Order History Section */}
            <div className={styles.orderHistorySection}>
                <h3>Order History</h3>
                <hr/>
                {/* Order Table */}
                {orders.length > 0 ? (
                    <table className={styles.orderTable}>
                        <thead>
                            <tr>
                                <th>Index</th>
                                <th>Restaurant Name</th>
                                <th>Order Time</th>
                                <th>Status</th>
                                <th>Total Amount</th>
                                <th>Delivery Address</th>
                            </tr>
                        </thead>
                        <tbody>
                            {orders.map((order, index) => (
                                <tr key={order.id}>
                                    <td>{index + 1}</td>
                                    <td>{order.restaurantName}</td>
                                    <td>{new Date(order.orderTime).toLocaleString()}</td>
                                    <td>{order.orderStatus}</td>
                                    <td>{order.totalAmount}</td>
                                    <td>{order.deliveryAddress}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ) : (
                    <p>No orders found.</p>
                )}
            </div>
        </div>
    );
};

export default CustomerTrackOrder;
