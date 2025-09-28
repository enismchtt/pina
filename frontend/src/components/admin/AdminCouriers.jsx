import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api_access/api';
import './AdminCouriers.css';

const AdminCouriers = () => {
  const [couriers, setCouriers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    API.get('/admin/couriers')
      .then(res => setCouriers(res.data))
      .catch(err => console.error('API error:', err));
  }, []);

  return (
    <div className="courier-table-wrapper" style={{ padding: '1rem', backgroundColor: '#f9f9f9' }}>
      <div className="search-wrapper" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1rem' }}>
        <span role="img" aria-label="search">🔍</span>
        <input
          type="text"
          placeholder="Search by username"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{
            marginBottom: '0.5rem',
            padding: '0.6rem 1rem',
            fontSize: '1rem',
            borderRadius: '6px',
            border: '1px solid #ccc',
            outline: 'none',
            width: '300px',
            boxShadow: '0 0 4px rgba(0,0,0,0.1)'
          }}
        />
      </div>
      <table className="courier-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {couriers
            .filter(c => c.approved && c.username.toLowerCase().includes(searchTerm.toLowerCase()))
            .map(c => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.username}</td>
                <td>{c.email}</td>
                <td>
                  <button
                    className="details-btn"
                    onClick={() => navigate(`/admin/couriers/${c.id}`)}
                  >
                    Details ➔
                  </button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminCouriers;