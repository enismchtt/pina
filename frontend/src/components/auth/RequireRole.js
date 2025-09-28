import React from 'react';
import { Navigate } from 'react-router-dom';

const RequireRole = ({ allowedRoles, children }) => {
    const accountType = localStorage.getItem('accountType');

    if (!allowedRoles.includes(accountType)) {
        return <Navigate to="/unauthorized" replace />;
    }

    return children;
};

export default RequireRole;
