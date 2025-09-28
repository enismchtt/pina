import axios from 'axios';

// In development with Docker, we need to use the configured backend URL
// Default to '/api' as fallback for production build
const baseURL = process.env.REACT_APP_API_URL || '/api';

console.log('API is using baseURL:', baseURL);

const API = axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
});

// Request interceptor
API.interceptors.request.use(
    (config) => {
        console.log('Making request to:', config.url);
        return config;
    },
    (error) => {
        console.error('Request error:', error);
        return Promise.reject(error);
    }
);

// Response interceptor
API.interceptors.response.use(
    (response) => {
        console.log('Response received:', response);
        return response;
    },
    (error) => {
        if (error.code === 'ERR_NETWORK') {
            console.error('Network Error: Could not connect to the server. Is the backend running?');
        } else {
            console.error('API Error:', error);
        }
        return Promise.reject(error);
    }
);

export default API;