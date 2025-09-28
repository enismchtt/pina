import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './CourierHome.css';
import CourierService from "../services/courierService";
import OrderService from "../services/orderService";

const CourierHome = ({ onLogout }) => {
    const [user, setUser] = useState(null);
    const [deliveries, setDeliveries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [availability, setAvailability] = useState(false);
    const [courierId, setCourierId] = useState(null);
    const [selectedStatus, setSelectedStatus] = useState("PICKED_UP");

    const [selectedOrder, setSelectedOrder] = useState(null);
    const closeModal = () => setSelectedOrder(null);

    const navigate = useNavigate();

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        const accountType = localStorage.getItem('accountType');

        if (!userId || accountType !== 'courier') {
            navigate('/login');
            return;
        }

        const fetchCourier = async () => {
            try {
                const res = await CourierService.getCourier(userId);
                const courier = res.data;

                if (courier) {
                    setCourierId(courier.id);
                    setAvailability(courier.available);
                    setUser(courier);

                    const deliveryRes = await CourierService.getCourierDeliveries(courier.id);
                    setDeliveries(deliveryRes.data);
                }
            } catch (err) {
                console.error('Error fetching courier data:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchCourier();
    }, [navigate]);

    const toggleAvailability = async () => {
        try {
            await CourierService.setAvailability(courierId, !availability);
            setAvailability(!availability);
        } catch (err) {
            console.error('Failed to toggle availability:', err);
        }
    };

    const updateStatus = async (id, newStatus) => {
        try {
            console.log(`Updating delivery #${id} status to: ${newStatus}`);
            await OrderService.updateOrderStatus(id,newStatus);
            if (newStatus === "PENDING"){
                await OrderService.unassignCourier(id);
                setDeliveries(prev => prev.filter(d => d.id !== id));
            }
            setDeliveries(prev =>
                prev.map(d => d.id === id ? { ...d, orderStatus: newStatus } : d)
            );

        } catch (err) {
            console.error("Failed to update delivery status:", err);
        }
    };

    const handleAccept = (id) => updateStatus(id, "WAITING_FOR_COURIER");
    const handleReject = (id) => updateStatus(id, "PENDING");
    const handleAbort = (id) => updateStatus(id, "PENDING");

    const handleSubmitStatus = (id) => {
        if (!selectedStatus) return;
        updateStatus(id, selectedStatus);
    };

    const categorized = {
        waiting: deliveries.filter(d => d.orderStatus === 'READY_FOR_PICKUP'),
        active: deliveries.filter(d => ['WAITING_FOR_COURIER', 'PICKED_UP'].includes(d.orderStatus)),
        past: deliveries.filter(d => ['DELIVERED', 'CANCELLED'].includes(d.orderStatus)),
    };

    return (
        <div className="courier-home">
            <header className="courier-header">
                <div className="header-left">
                    <h1>pina</h1>
                    <span className="account-type">Courier</span>
                </div>
                <div className="header-right">
                    {user ? (
                        <span className="username">Welcome, {user.username}</span>
                    ) : (
                        <span className="username">Loading...</span>
                    )}
                    <button className="logout-button" onClick={onLogout}>Logout</button>
                </div>
            </header>

            <div className="main-content">
                <div className="content-header">
                    <h2>Your Deliveries</h2>
                    <button
                        className={`availability-toggle ${availability ? 'offline' : 'online'}`}
                        onClick={toggleAvailability}>
                        {availability ? 'Go Offline' : 'Go Online'}
                    </button>
                </div>
                <div className="delivery-sections">
                    <div className="delivery-column">
                        <h3>Waiting for Acceptance</h3>
                        {categorized.waiting.map(order => (
                            <div className="delivery-card" key={order.id}>
                                <div onClick={() => setSelectedOrder(order)}>
                                    <h3>Order #{order.id}</h3>
                                    <p><strong>Customer:</strong> {order.customerName}</p>
                                    <p><strong>Restaurant:</strong> {order.restaurantName}</p>
                                    <p><strong>Status:</strong> {order.orderStatus}</p>
                                </div>
                                <button onClick={() => handleAccept(order.id)}>Accept</button>
                                <button onClick={() => handleReject(order.id)} className="reject-btn">Reject</button>
                            </div>
                        ))}
                    </div>

                    <div className="delivery-column">
                        <h3>Active Deliveries</h3>
                        {categorized.active.map(order => (
                            <div className="delivery-card" key={order.id}>
                                <div onClick={() => setSelectedOrder(order)}>
                                    <h3>Order #{order.id}</h3>
                                    <p><strong>Customer:</strong> {order.customerName}</p>
                                    <p><strong>Restaurant:</strong> {order.restaurantName}</p>
                                    <p><strong>Status:</strong> {order.orderStatus}</p>
                                </div>
                                <select
                                    value={selectedStatus}
                                    onChange={(e) =>
                                        setSelectedStatus(e.target.value)
                                    }>
                                    <option value="PICKED_UP">PICKED UP</option>
                                    <option value="DELIVERED">DELIVERED</option>
                                </select>
                                <button onClick={() => handleSubmitStatus(order.id)}>Submit</button>
                                <button onClick={() => handleAbort(order.id)} className="abort-btn">Abort</button>
                            </div>
                        ))}
                    </div>

                    <div className="delivery-column">
                        <h3>Past Deliveries</h3>
                        {categorized.past.map(order => (
                            <div className="delivery-card" key={order.id}>
                                <div onClick={() => setSelectedOrder(order)}>
                                    <h3>Order #{order.id}</h3>
                                    <p><strong>Status:</strong> {order.orderStatus}</p>
                                    <p><strong>Customer:</strong> {order.customerName}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
                {selectedOrder && (
                    <div className="modal-overlay" onClick={closeModal}>
                        <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                            <h2>Order Details - #{selectedOrder.id}</h2>
                            <p><strong>Customer:</strong> {selectedOrder.customerName}</p>
                            <p><strong>Restaurant:</strong> {selectedOrder.restaurantName}</p>
                            <p><strong>Delivery Address:</strong> {selectedOrder.deliveryAddress}</p>
                            <p><strong>Customer Note:</strong> {selectedOrder.specialInstructions}</p>
                            <p><strong>Total:</strong> ${selectedOrder.totalAmount}</p>
                            <p><strong>Status:</strong> {selectedOrder.orderStatus}</p>
                            <p><strong>Ordered at:</strong> {new Date(selectedOrder.orderTime).toLocaleString()}</p>
                            {selectedOrder.deliveryTime && (
                                <p><strong>Delivered at:</strong> {new Date(selectedOrder.deliveryTime).toLocaleString()}</p>
                            )}
                            <button onClick={closeModal}>Close</button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default CourierHome;
