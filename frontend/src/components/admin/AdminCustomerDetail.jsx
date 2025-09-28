import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../api_access/api';
import './AdminCouriers.css';

const AdminCustomerDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [customer, setCustomer] = useState(null);

    const fetchCustomer = async () => {
        try {
            const res = await API.get(`/customers/${id}`);
            setCustomer(res.data);
        } catch (err) {
            console.error('Error fetching customer:', err);
        }
    };

    useEffect(() => {
        fetchCustomer();
    }, [id]);

    const toggleBanStatus = async () => {
        try {
            const endpoint = customer.banned ? `/admin/unban/${customer.id}` : `/admin/ban/${customer.id}`;
            await API.put(endpoint);
            fetchCustomer();
        } catch (err) {
            console.error('Ban toggle error:', err);
        }
    };

    const deleteCustomer = async () => {
        const confirmDelete = window.confirm('Are you sure you want to delete this customer? This action cannot be undone.');
        if (!confirmDelete) return;

        try {
            await API.delete(`/customers/${customer.id}`);
            alert('Customer deleted successfully.');
            navigate('/admin'); // veya uygun başka bir sayfa
        } catch (err) {
            console.error('Error deleting customer:', err);
            alert('Failed to delete customer.');
        }
    };

    if (!customer) return <div>Loading customer data...</div>;

    return (
        <div className="courier-table-wrapper">
            <h2>Customer Details</h2>
            <table className="courier-table">
                <tbody>
                <tr><td><strong>ID:</strong></td><td>{customer.id}</td></tr>
                <tr><td><strong>Username:</strong></td><td>{customer.username}</td></tr>
                <tr><td><strong>Email:</strong></td><td>{customer.email}</td></tr>
                <tr><td><strong>Phone Number:</strong></td><td>{customer.phoneNumber}</td></tr>
                <tr><td><strong>First Name:</strong></td><td>{customer.firstName}</td></tr>
                <tr><td><strong>Last Name:</strong></td><td>{customer.lastName}</td></tr>
                <tr><td><strong>Address:</strong></td><td>{customer.address}</td></tr>
                <tr><td><strong>Rating:</strong></td><td>{customer.rating}</td></tr>
                <tr><td><strong>Active:</strong></td><td>{customer.active ? 'Yes' : 'No'}</td></tr>
                <tr>
                    <td><strong>Banned:</strong></td>
                    <td style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                        {customer.banned ? 'Yes' : 'No'}
                        <button
                            onClick={toggleBanStatus}
                            style={{
                                padding: '6px 12px',
                                borderRadius: '4px',
                                border: 'none',
                                cursor: 'pointer',
                                color: 'white',
                                fontWeight: 500,
                                backgroundColor: customer.banned ? '#4CAF50' : '#ff4d4d'
                            }}
                        >
                            {customer.banned ? 'Unban' : 'Ban'}
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
                    onClick={deleteCustomer}
                >
                    Delete Customer
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

export default AdminCustomerDetail;