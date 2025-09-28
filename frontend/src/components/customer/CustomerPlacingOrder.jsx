

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import OrderService from '../services/orderService';

import './CustomerPlacingOrder.css';

const CustomerPlacingOrder = () => {



    //local components
    const [cartItems, setCartItems] = useState([]);
    const [orderItems, setOrderItems] = useState([]);
    const [customer, setCustomer] = useState(null);
    const [restaurant, setRestaurant] = useState(null);

    const [total ,setTotal] = useState(0);

    const [noteToCourier, setNoteToCourier] = useState('');



    useEffect(() => {
        // 1) Load cartItems from localStorage
        const rawCart = localStorage.getItem('cartItems');
        const parsedCart = rawCart ? JSON.parse(rawCart) : [];
        setCartItems(parsedCart);
    
        // 2) Build OrderItemDTO objects in one go
        const dtos = parsedCart.map(item => ({
          quantity: item.quantity,
          price: item.price,
          specialInstructions: item.customizationNote || '',
          menuItemId: item.id,
          menuItemName: item.name,
        }));
        setOrderItems(dtos);
    
        // 3) Load customer object
        const rawCustomer = localStorage.getItem('customer');
        if (rawCustomer) {
          try {
            setCustomer(JSON.parse(rawCustomer));
          } catch (err) {
            console.error('Error parsing customer from storage', err);
          }
        }
    
        // 4) Load restaurant object
        const rawRestaurant = localStorage.getItem('restaurant');
        if (rawRestaurant) {
          try {
            setRestaurant(JSON.parse(rawRestaurant));
          } catch (err) {
            console.error('Error parsing restaurant from storage', err);
          }
        }

        // 4) Load ottal
        const rawtotal = localStorage.getItem('totalprice');
        if (rawtotal) {
          try {
            setTotal(rawtotal);
          } catch (err) {
            console.error('Error parsing total', err);
          }
        }



      }, []);


// Log orderItems whenever they change
    useEffect(() => {
    console.log('OrderItems:', orderItems);
    console.log("type of ",typeof(orderItems))
    }, [orderItems]);


    const handlePlaceOrder = async () => {
        const res = await OrderService.placeOrder(customer.id, restaurant.id, customer.address, orderItems);
        const order = res.data
        await OrderService.setOrderNote(order.id, noteToCourier);
        console.log("order is placed.")
        localStorage.setItem("cartItems", []);
        setOrderItems([]);
    };







    return(
        <div className="customer-placing-order">
        <header className="h">
          <div className="h-left">
            <h1>Pina</h1>
            <div className="info-user">
              <span className="usrname">{customer?.username}</span>
              <span className="account-type">Customer</span>
            </div>
          </div>
        </header>
  
        <main className="order-details">
        <h2>Order Details</h2>
        {orderItems.length === 0 ? (
          <p>No items to display.</p>
        ) : (
          <>
            <ul className="order-list">
              {orderItems.map((item, idx) => (
                <li key={idx} className="order-item">
                  <div><strong>Name:</strong> {item.menuItemName}</div>
                  <div><strong>Quantity:</strong> {item.quantity}</div>
                  <div><strong>Price:</strong> {item.price.toFixed(2)} TL </div>
                  {item.specialInstructions && (
                    <div><strong>Note:</strong> {item.specialInstructions}</div>
                  )}
                </li>
              ))}
            </ul>
              <div className="courier-note">
                  <h3>Note To Courier</h3>
                  <textarea
                      placeholder="Add any instructions for the courier..."
                      value={noteToCourier}
                      onChange={(e) => setNoteToCourier(e.target.value)}
                  />
              </div>
            <div className="order-total">
              <strong>Total:</strong> {total} TL
            </div>
          </>
        )}
      </main>

        <div className="order-now-section">
          <button
            className="order-now-button"
            onClick={handlePlaceOrder}
            disabled={orderItems.length === 0}
          >
            Order Now !
          </button>
        </div>
      </div>
        

    );







}; 



export default CustomerPlacingOrder;