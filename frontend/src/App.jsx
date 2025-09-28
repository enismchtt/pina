import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import Login from './components/auth/Login';
import Register from './components/auth/Register';
import CustomerHome from './components/customer/CustomerHome';
import CustomerOrder from './components/customer/CustomerOrder';
import CustomerPlacingOrder from './components/customer/CustomerPlacingOrder';
import CustomerTrackOrder from './components/customer/CustomerTrackOrder';
import RestaurantHome from './components/restaurant/RestaurantHome';
import CourierHome from './components/courier/CourierHome';
import CustomerManageProfile from './components/customer/CustomerManageProfile';

import AdminDashboard from './components/admin/AdminDashboard';
import AdminCouriers from './components/admin/AdminCouriers';
import AdminRestaurants from './components/admin/AdminRestaurants';
import AdminCustomers from './components/admin/AdminCustomers';
import AdminReviews from './components/admin/AdminReviews';
import AdminOrders from './components/admin/AdminOrders';
import AdminPendingApprovals from './components/admin/AdminPendingApprovals';
import AdminCustomerDetail from './components/admin/AdminCustomerDetail';
import AdminRestaurantDetail from './components/admin/AdminRestaurantDetail';
import AdminCourierDetail from './components/admin/AdminCourierDetail';

import Unauthorized from './components/auth/Unauthorized';
import RequireRole from './components/auth/RequireRole';

import './App.css';
import RestaurantProfile from "./components/restaurant/RestaurantProfile";

function App() {
  return (
      <Router>
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/unauthorized" element={<Unauthorized />} />

          {/* Customer Routes */}
          <Route path="/customer" element={<RequireRole allowedRoles={['customer']}><CustomerHome /></RequireRole>} />
          <Route path="/customer/restaurant/:restaurantId" element={<RequireRole allowedRoles={['customer']}><CustomerOrder /></RequireRole>} />
          <Route path="/customer/place-order" element={<RequireRole allowedRoles={['customer']}><CustomerPlacingOrder /></RequireRole>} />
          <Route path="/customer/track-order" element={<RequireRole allowedRoles={['customer']}><CustomerTrackOrder /></RequireRole>} />
          <Route path="/customer/manage-profile" element={<RequireRole allowedRoles={['customer']}><CustomerManageProfile /></RequireRole>} />

          {/* Restaurant Routes */}
          <Route path="/restaurant" element={<RequireRole allowedRoles={['restaurant']}><RestaurantHome /></RequireRole>} />
          <Route path="/restaurant/manage-profile" element={<RequireRole allowedRoles={['restaurant']}><RestaurantProfile /></RequireRole>} />

          {/* Courier Routes */}
          <Route path="/courier" element={<RequireRole allowedRoles={['courier']}><CourierHome /></RequireRole>} />

          {/* Admin Routes */}
          <Route path="/admin" element={<RequireRole allowedRoles={['admin']}><AdminDashboard /></RequireRole>} />
          <Route path="/admin/couriers" element={<RequireRole allowedRoles={['admin']}><AdminCouriers /></RequireRole>} />
          <Route path="/admin/restaurants" element={<RequireRole allowedRoles={['admin']}><AdminRestaurants /></RequireRole>} />
          <Route path="/admin/customers" element={<RequireRole allowedRoles={['admin']}><AdminCustomers /></RequireRole>} />
          <Route path="/admin/reviews" element={<RequireRole allowedRoles={['admin']}><AdminReviews /></RequireRole>} />
          <Route path="/admin/approvals" element={<RequireRole allowedRoles={['admin']}><AdminPendingApprovals /></RequireRole>} />
          <Route path="/admin/orders" element={<RequireRole allowedRoles={['admin']}><AdminOrders /></RequireRole>} />
          <Route path="/admin/customers/:id" element={<RequireRole allowedRoles={['admin']}><AdminCustomerDetail /></RequireRole>} />
          <Route path="/admin/restaurants/:id" element={<RequireRole allowedRoles={['admin']}><AdminRestaurantDetail /></RequireRole>} />
          <Route path="/admin/couriers/:id" element={<RequireRole allowedRoles={['admin']}><AdminCourierDetail /></RequireRole>} />
        </Routes>
      </Router>
  );
}

export default App;
