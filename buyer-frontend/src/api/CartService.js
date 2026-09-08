import axios from 'axios';

const API_URL = "http://localhost:9090/api/cart";

// تابع کمکی برای گرفتن توکن و تنظیم هدر
const getHeaders = () => {
    const token = localStorage.getItem("token");
    return {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };
};

export const fetchCartApi = () => axios.get(API_URL, getHeaders());

export const addToCartApi = (productId, quantity) =>
    axios.post(`${API_URL}/items`, { productId, quantity }, getHeaders());

export const updateCartItemApi = (productId, quantity) =>
    axios.put(`${API_URL}/items`, { productId, quantity }, getHeaders());

export const removeCartItemApi = (productId) =>
    axios.delete(`${API_URL}/items/${productId}`, getHeaders());

export const checkoutApi = (items) =>
    axios.post(`${API_URL}/checkout`, { items }, getHeaders());
