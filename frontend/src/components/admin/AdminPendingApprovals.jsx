import React, { useEffect, useState } from 'react';
import API from '../api_access/api';
import './AdminCouriers.css';

const PendingApprovals = () => {
  const [pendingList, setPendingList] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [couriersRes, restaurantsRes] = await Promise.all([
          API.get('/admin/pending-couriers'),
          API.get('/admin/pending-restaurants'),
        ]);

        const merged = [
          ...couriersRes.data.map(c => ({ ...c, type: 'courier', displayName: `${c.firstName} ${c.lastName}` })),
          ...restaurantsRes.data.map(r => ({ ...r, type: 'restaurant', displayName: r.name })),
        ];

        setPendingList(merged);
      } catch (err) {
        console.error('Fetch error:', err);
      }
    };

    fetchData();
  }, []);

  const refreshData = async () => {
    const [couriersRes, restaurantsRes] = await Promise.all([
      API.get('/admin/pending-couriers'),
      API.get('/admin/pending-restaurants'),
    ]);
    const merged = [
      ...couriersRes.data.map(c => ({ ...c, type: 'courier', displayName: `${c.firstName} ${c.lastName}` })),
      ...restaurantsRes.data.map(r => ({ ...r, type: 'restaurant', displayName: r.name })),
    ];
    setPendingList(merged);
  };

  const approve = async (id, type) => {
    await API.put(`/admin/approve-${type}/${id}`);
    refreshData();
  };

  const reject = async (id, type) => {
    await API.delete(`/admin/reject-${type}/${id}`);
    refreshData();
  };

  return (
    <div className="courier-table-wrapper">
      <h2>Pending Approvals</h2>
      <table className="courier-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Type</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {pendingList.map(item => (
            <tr key={`${item.type}-${item.id}`}>
              <td>{item.id}</td>
              <td>{item.username}</td>
              <td>{item.type === 'courier' ? 'Courier' : item.type === 'restaurant' ? 'Restaurant' : 'Unknown'}</td>
              <td>{item.email}</td>
              <td>
              <button className="approve-btn" onClick={() => approve(item.id, item.type)}>Approve</button>
              <button className="reject-btn" onClick={() => reject(item.id, item.type)}>Reject</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PendingApprovals;