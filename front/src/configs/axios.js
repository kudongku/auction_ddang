import axios from 'axios';

export const Axios = axios.create({
    withCredentials: true,
    baseURL: 'http://localhost:8080',
    headers: {'Content-Type': 'application/json'}
});

// Set the AUTH token for any request
Axios.interceptors.request.use(function (config) {
    const token = localStorage.getItem('authorizationToken');
    if (token) {
        config.headers.Authorization = token;
    }
    return config;
}, function (error) {
    return Promise.reject(error);
});
