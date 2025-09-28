import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../api_access/api';
import './AdminCouriers.css';

const AdminCourierDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [courier, setCourier] = useState(null);

    const fetchCourier = async () => {
        try {
            const res = await API.get(`/admin/couriers/${id}`);
            setCourier(res.data);
        } catch (err) {
            console.error('Error fetching courier:', err);
        }
    };

    useEffect(() => {
        fetchCourier();
    }, [id]);

    const toggleBanStatus = async () => {
        try {
            const endpoint = courier.banned ? `/admin/unban/${courier.id}` : `/admin/ban/${courier.id}`;
            await API.put(endpoint);
            fetchCourier();
        } catch (err) {
            console.error('Ban toggle error:', err);
        }
    };

    const deleteCourier = async () => {
        const confirmDelete = window.confirm('Are you sure you want to delete this courier? This action cannot be undone.');
        if (!confirmDelete) return;

        try {
            await API.delete(`/couriers/${courier.id}`); // NOT: endpoint admin değil, doğrudan /couriers/
            alert('Courier deleted successfully.');
            navigate('/admin'); // veya uygun bir geri dönüş sayfası
        } catch (err) {
            console.error('Error deleting courier:', err);
            alert('Failed to delete courier.');
        }
    };

    if (!courier) return <div>Loading courier data...</div>;

    return (
        <div className="courier-table-wrapper">
            <h2>Courier Details</h2>
            <table className="courier-table">
                <tbody>
                <tr><td><strong>ID:</strong></td><td>{courier.id}</td></tr>
                <tr><td><strong>Username:</strong></td><td>{courier.username}</td></tr>
                <tr><td><strong>Email:</strong></td><td>{courier.email}</td></tr>
                <tr><td><strong>Phone Number:</strong></td><td>{courier.phoneNumber}</td></tr>
                <tr><td><strong>First Name:</strong></td><td>{courier.firstName}</td></tr>
                <tr><td><strong>Last Name:</strong></td><td>{courier.lastName}</td></tr>
                <tr><td><strong>Address:</strong></td><td>{courier.address}</td></tr>
                <tr><td><strong>Vehicle Info:</strong></td><td>{courier.vehicleInfo}</td></tr>
                <tr><td><strong>Approved:</strong></td><td>{courier.approved ? 'Yes' : 'No'}</td></tr>
                <tr><td><strong>Available:</strong></td><td>{courier.available ? 'Yes' : 'No'}</td></tr>
                <tr><td><strong>Rating:</strong></td><td>{courier.rating}</td></tr>
                <tr>
                    <td><strong>Banned:</strong></td>
                    <td style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                        {courier.banned ? 'Yes' : 'No'}
                        <button
                            onClick={toggleBanStatus}
                            style={{
                                padding: '6px 12px',
                                borderRadius: '4px',
                                border: 'none',
                                cursor: 'pointer',
                                color: 'white',
                                fontWeight: 500,
                                backgroundColor: courier.banned ? '#4CAF50' : '#ff4d4d'
                            }}
                        >
                            {courier.banned ? 'Unban' : 'Ban'}
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
                    onClick={deleteCourier}
                >
                    Delete Courier
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

export default AdminCourierDetail;