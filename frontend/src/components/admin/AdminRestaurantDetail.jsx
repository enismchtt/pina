import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../api_access/api';
import './AdminCouriers.css';

const AdminRestaurantDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [restaurant, setRestaurant] = useState(null);

    const fetchRestaurant = async () => {
        try {
            const res = await API.get(`/restaurants/${id}`);
            setRestaurant(res.data);
        } catch (err) {
            console.error('Error fetching restaurant:', err);
        }
    };

    useEffect(() => {
        fetchRestaurant();
    }, [id]);

    const toggleBanStatus = async () => {
        try {
            const endpoint = restaurant.banned
                ? `/admin/unban/${restaurant.id}`
                : `/admin/ban/${restaurant.id}`;
            await API.put(endpoint);
            fetchRestaurant();
        } catch (err) {
            console.error('Ban toggle error:', err);
        }
    };

    const deleteRestaurant = async () => {
        const confirmed = window.confirm('Are you sure you want to delete this restaurant? This action cannot be undone.');
        if (!confirmed) return;

        try {
            await API.delete(`/restaurants/${restaurant.id}`);
            alert('Restaurant deleted successfully.');
            navigate('/admin'); // veya uygun başka bir yol
        } catch (err) {
            console.error('Error deleting restaurant:', err);
            alert('Failed to delete restaurant.');
        }
    };

    if (!restaurant) return <div>Loading restaurant data...</div>;

    return (
        <div className="courier-table-wrapper">
            <h2>Restaurant Details</h2>
            <table className="courier-table">
                <tbody>
                <tr><td><strong>ID:</strong></td><td>{restaurant.id}</td></tr>
                <tr><td><strong>Username:</strong></td><td>{restaurant.username}</td></tr>
                <tr><td><strong>Email:</strong></td><td>{restaurant.email}</td></tr>
                <tr><td><strong>Name:</strong></td><td>{restaurant.name}</td></tr>
                <tr><td><strong>Description:</strong></td><td>{restaurant.description}</td></tr>
                <tr><td><strong>Address:</strong></td><td>{restaurant.address}</td></tr>
                <tr><td><strong>Cuisine Type:</strong></td><td>{restaurant.cuisineType}</td></tr>
                <tr><td><strong>Business Hours:</strong></td><td>{restaurant.businessHours}</td></tr>
                <tr><td><strong>Rating:</strong></td><td>{restaurant.rating}</td></tr>
                <tr><td><strong>Approved:</strong></td><td>{restaurant.approved ? 'Yes' : 'No'}</td></tr>
                <tr>
                    <td><strong>Banned:</strong></td>
                    <td style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                        {restaurant.banned ? 'Yes' : 'No'}
                        <button
                            onClick={toggleBanStatus}
                            style={{
                                padding: '6px 12px',
                                borderRadius: '4px',
                                border: 'none',
                                cursor: 'pointer',
                                color: 'white',
                                fontWeight: 500,
                                backgroundColor: restaurant.banned ? '#4CAF50' : '#ff4d4d'
                            }}
                        >
                            {restaurant.banned ? 'Unban' : 'Ban'}
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>

            <div style={{ marginTop: '1rem', display: 'flex', gap: '10px' }}>
                <button
                    style={{
                        backgroundColor: '#d9534f',
                        color: 'white',
                        padding: '6px 12px',
                        borderRadius: '4px',
                        border: 'none',
                        cursor: 'pointer',
                        fontWeight: 500
                    }}
                    onClick={deleteRestaurant}
                >
                    Delete Restaurant
                </button>

                <button
                    style={{
                        backgroundColor: '#aaa',
                        color: 'white',
                        padding: '6px 12px',
                        borderRadius: '4px',
                        border: 'none',
                        cursor: 'pointer',
                        fontWeight: 500
                    }}
                    onClick={() => navigate(-1)}
                >
                    Back
                </button>
            </div>
        </div>
    );
};

export default AdminRestaurantDetail;