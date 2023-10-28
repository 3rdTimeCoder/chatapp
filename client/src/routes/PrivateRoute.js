import React from 'react';
import { Route, Navigate } from 'react-router-dom';

const PrivateRoute = ({ component: Component, isAuthenticated }) => {
    if (!isAuthenticated) {
        return <Navigate to="/" replace />
    }
    return <Component/>;
}
  
export default PrivateRoute;