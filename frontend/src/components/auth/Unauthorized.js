import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Unauthorized.css';

const Unauthorized = () => {
    const navigate = useNavigate();

    return (
        <div className="unauth-container">
            <div className="unauth-header">
                <h1>Access Denied</h1>
            </div>
            <div className="unauth-content">
                <div className="unauth-welcome-section">
                    <h2>403 - Unauthorized</h2>
                    <h3>Oops! You don’t have permission to view this page.</h3>
                    <p>Please return to a page that matches your account type, or log out and try again with the correct account.</p>
                    <button
                        className="unauth-go-back-button"
                        onClick={() => navigate(-1)}
                    >
                        Go Back
                    </button>
                    <button
                        className="unauth-go-login-button"
                        onClick={() => navigate('/login')}
                    >
                        Go to Login Page
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Unauthorized;
