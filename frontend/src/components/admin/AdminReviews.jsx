import React, { useEffect, useState } from 'react';
import API from '../api_access/api';
import './AdminCouriers.css';

const AdminReviews = () => {
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    API.get('/admin/reviews')
      .then(res => setReviews(res.data))
      .catch(err => console.error('Review fetch error:', err));
  }, []);

  return (
    <div className="courier-table-wrapper">
      <h2>Reviews</h2>
      <table className="courier-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Rating</th>
            <th>Comment</th>
            <th>Time</th>
            <th>Type</th>
            <th>Customer</th>
            <th>Restaurant</th>
            <th>Courier</th>
          </tr>
        </thead>
        <tbody>
          {reviews.map(r => (
            <tr key={r.id}>
              <td>{r.id}</td>
              <td>{r.rating}</td>
              <td>{r.comment}</td>
              <td>{r.reviewTime}</td>
              <td>{r.reviewType}</td>
              <td>{r.customerId ?? 'N/A'}</td>
              <td>{r.restaurantId ?? 'N/A'}</td>
              <td>{r.courierId ?? 'N/A'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminReviews;