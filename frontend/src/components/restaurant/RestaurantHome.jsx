import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './RestaurantHome.css';
import RestaurantService from '../services/restaurantService';
import OrderService from '../services/orderService';
import CourierService from '../services/courierService';

const RestaurantHome = ({ onLogout }) => {
  const [restaurant, setRestaurant] = useState(null);
  const [menuItems, setMenuItems] = useState([]);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [availableCouriers, setAvailableCouriers] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [selectedCourier, setSelectedCourier] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [menuItemData, setMenuItemData] = useState({
    name: '',
    description: '',
    price: 0,
    available: true,
    category: 'HAMBURGER',
    imageUrl: '',
    restaurantId: 2, // will be updated in useEffect
  });

  const navigate = useNavigate();

  useEffect(() => {
    const restaurantId = localStorage.getItem('userId');
    const accountType = localStorage.getItem('accountType');

    if (!restaurantId || accountType !== 'restaurant') {
      navigate('/login');
      return;
    }

    setMenuItemData((prev) => ({ ...prev, restaurantId }));

    const fetchRestaurantData = async () => {
      try {
        const res = await RestaurantService.getRestaurant(restaurantId);
        setRestaurant(res.data);

        const menuRes = await RestaurantService.getRestaurantMenu(restaurantId);
        setMenuItems(menuRes.data);

        const orderRes = await OrderService.getOrdersByRestaurant(restaurantId);
        setOrders(orderRes.data);

        const couriersRes = await CourierService.getAllCouriers();
        setAvailableCouriers(couriersRes.data);
      } catch (err) {
        console.error('Error fetching restaurant data:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurantData();
  }, [navigate]);

  const updateOrderStatus = async (orderId, newStatus) => {
    try {
      await OrderService.updateOrderStatus(orderId, newStatus);
      setOrders((prevOrders) =>
          prevOrders.map((order) =>
              order.id === orderId ? { ...order, orderStatus: newStatus } : order
          )
      );
    } catch (err) {
      console.error('Failed to update order status:', err);
    }
  };

  const assignCourierToOrder = async (orderId, courierId) => {
    try {
      await OrderService.assignCourier(orderId, courierId);
      setOrders((prevOrders) =>
          prevOrders.map((order) =>
              order.id === orderId ? { ...order, courierId } : order
          )
      );
    } catch (err) {
      console.error('Failed to assign courier:', err);
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setMenuItemData((prevState) => ({
      ...prevState,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = async () => {
    try {
      await RestaurantService.addMenuItem(restaurant.id, menuItemData);
      setShowModal(false);
      alert('Menu item added successfully');
    } catch (err) {
      console.error('Error adding menu item:', err);
    }
  };

  const closeModal = () => setSelectedOrder(null);

  return (
      <div className="restaurant-home">
        <nav className="restaurant-navbar">
          <div className="navbar-left">
            <span className="logo">
              <span className="pina-brand">Pina</span> {restaurant?.name}
            </span>
          </div>
          <div className="navbar-center">
            <button onClick={() => setShowModal(true)} className="navbar-button">menu</button>
            <button onClick={() => navigate('/restaurant/manage-profile')} className="navbar-button">profile</button>

            <button onClick={() => navigate('/restaurant/reviews')} className="navbar-button">reviews</button>
          </div>
          <div className="navbar-right">
            <button className="logout-button" onClick={onLogout}>Logout</button>
          </div>
        </nav>

        <div className="main-content">
          {showModal && (
              <div className="modal-overlay" onClick={() => setShowModal(false)}>
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                  <h2>Create New Menu Item</h2>
                  <form>
                    <div>
                      <label>Item Name</label>
                      <input type="text" name="name" value={menuItemData.name} onChange={handleInputChange} />
                    </div>
                    <div>
                      <label>Description</label>
                      <input type="text" name="description" value={menuItemData.description} onChange={handleInputChange} />
                    </div>
                    <div>
                      <label>Price</label>
                      <input type="number" name="price" value={menuItemData.price} onChange={handleInputChange} />
                    </div>
                    <div>
                      <label>Category</label>
                      <select name="category" value={menuItemData.category} onChange={handleInputChange}>
                        <option value="HAMBURGER">Hamburger</option>
                        <option value="PIZZA">Pizza</option>
                        <option value="PASTA">Pasta</option>
                      </select>
                    </div>
                    <div>
                      <label>Image URL</label>
                      <input type="text" name="imageUrl" value={menuItemData.imageUrl} onChange={handleInputChange} />
                    </div>
                    <div>
                      <label>Available</label>
                      <input type="checkbox" name="available" checked={menuItemData.available} onChange={handleInputChange} />
                    </div>
                    <div>
                      <button type="button" onClick={handleSubmit}>Add Menu Item</button>
                      <button type="button" onClick={() => setShowModal(false)}>Close</button>
                    </div>
                  </form>
                </div>
              </div>
          )}

          <div className="orders-section">
            <div className="order-column">
              <h3>Pending Orders</h3>
              {orders.filter(o => o.orderStatus === 'PENDING').map(order => (
                  <div className="order-card" key={order.id}>
                    <div onClick={() => setSelectedOrder(order)}>
                      <h3>Order #{order.id}</h3>
                      <p><strong>Customer:</strong> {order.customerName}</p>
                      <p><strong>Status:</strong> {order.orderStatus}</p>
                    </div>
                    <button onClick={() => updateOrderStatus(order.id, 'BEING_PREPARED')}>Start Preparation</button>
                    <button onClick={() => updateOrderStatus(order.id, 'CANCELLED')} className="cancel-btn">Cancel</button>
                  </div>
              ))}
            </div>

            <div className="order-column">
              <h3>Active Orders</h3>
              {orders.filter(o => o.orderStatus === 'BEING_PREPARED').map(order => (
                  <div className="order-card" key={order.id}>
                    <div onClick={() => setSelectedOrder(order)}>
                      <h3>Order #{order.id}</h3>
                      <p><strong>Customer:</strong> {order.customerName}</p>
                      <p><strong>Status:</strong> {order.orderStatus}</p>
                    </div>
                    <button onClick={() => updateOrderStatus(order.id, 'READY_FOR_PICKUP')}>Ready for Pickup</button>
                  </div>
              ))}
            </div>

            <div className="order-column">
              <h3>Completed Orders</h3>
              {orders.filter(o => o.orderStatus === 'READY_FOR_PICKUP').map(order => (
                  <div className="order-card" key={order.id}>
                    <div onClick={() => setSelectedOrder(order)}>
                      <h3>Order #{order.id}</h3>
                      <p><strong>Customer:</strong> {order.customerName}</p>
                      <p><strong>Status:</strong> {order.orderStatus}</p>
                    </div>
                    <select onChange={(e) => setSelectedCourier(e.target.value)}>
                      <option value="">Assign Courier</option>
                      {availableCouriers.map(courier => (
                          <option key={courier.id} value={courier.id}>{courier.username}</option>
                      ))}
                    </select>
                    <button onClick={() => assignCourierToOrder(order.id, selectedCourier)}>Assign Courier</button>
                  </div>
              ))}
            </div>
          </div>

          {selectedOrder && (
              <div className="modal-overlay" onClick={closeModal}>
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                  <h2>Order Details - #{selectedOrder.id}</h2>
                  <p><strong>Customer:</strong> {selectedOrder.customerName}</p>
                  <p><strong>Status:</strong> {selectedOrder.orderStatus}</p>
                  <button onClick={closeModal}>Close</button>
                </div>
              </div>
          )}
        </div>
      </div>
  );
};

export default RestaurantHome;
