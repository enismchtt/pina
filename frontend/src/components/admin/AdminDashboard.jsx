import React, { useState,useEffect } from 'react';
import AdminCouriers from './AdminCouriers';
import AdminCustomers from './AdminCustomers';
import AdminRestaurants from './AdminRestaurants';
import AdminOrders from './AdminOrders';
import AdminReviews from './AdminReviews';
import AdminPendingApprovals from './AdminPendingApprovals';
import API from "../api_access/api";
import './AdminDashboard.css';
import api from "../api_access/api";

const AdminDashboard = () => {
  const adminId = localStorage.getItem('userId');
  const [activeTab, setActiveTab] = useState('home');
  const [username, setUsername] = useState('');
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');

  useEffect(() => {
    const fetchUsername = async () => {
      try {
        const response = await api.get(`/admin/${adminId}`);
        setUsername(response.data);
      } catch (error) {
        console.error("Failed to fetch admin username", error);
      }
    };

    if (adminId) {
      fetchUsername();
    }
  }, [adminId]);


  const handleUpdate = async () => {
    try {
      const authPayload = {
        username,
        password: oldPassword
      };
      await API.post('/auth/check-password', authPayload);

      const passwordChangePayload = {
        username,
        newPassword
      };
      await API.post('/auth/change-password', passwordChangePayload);

      alert('Admin credentials updated successfully');
      setOldPassword('');
      setNewPassword('');
    } catch (err) {
      console.error('Error updating admin credentials', err);
      alert('Failed to update credentials');
    }
  };

  const renderTab = () => {
    switch (activeTab) {
      case 'home':
        return (
            <div className="admin-dashboard">
              <h2>Admin Credentials</h2>
              <form className="admin-form">
                <div className="form-group">
                  <label>Username:</label>
                  <input type="text" value={username} readOnly/>
                </div>
                <div className="form-group">
                  <label>New Password:</label>
                  <input
                      type="password"
                      value={newPassword}
                      onChange={e => setNewPassword(e.target.value)}
                  />
                </div>
                <div className="form-group">
                  <label>Confirm Current Password:</label>
                  <input
                      type="password"
                      value={oldPassword}
                      onChange={e => setOldPassword(e.target.value)}
                  />
                </div>
                <button type="button" onClick={handleUpdate}>
                  Save Changes
                </button>
              </form>
            </div>
        );
      case 'customers':
        return <AdminCustomers />;
      case 'couriers':
        return <AdminCouriers />;
      case 'restaurants':
        return <AdminRestaurants />;
      case 'orders':
        return <AdminOrders />;
      case 'reviews':
        return <AdminReviews />;
      case 'approvals':
        return <AdminPendingApprovals />;
      default:
        return <div className="empty-content">Select a tab.</div>;
    }
  };

  return (
    <div className="admin-dashboard-top">
      <header className="top-navbar">
        <span className="brand">Pina Admin</span>
        <nav className="top-nav-links">
          <button onClick={() => setActiveTab('home')}>Home</button>
          <button onClick={() => setActiveTab('customers')}>Customers</button>
          <button onClick={() => setActiveTab('couriers')}>Couriers</button>
          <button onClick={() => setActiveTab('restaurants')}>Restaurants</button>
          <button onClick={() => setActiveTab('orders')}>Orders</button>
          <button onClick={() => setActiveTab('reviews')}>Reviews</button>
          <button onClick={() => setActiveTab('approvals')}>Pending Approvals</button>
        </nav>
      </header>

      <div className="divider" />
      <main className="admin-main-content">
        {renderTab()}
      </main>
    </div>
  );
};

export default AdminDashboard;