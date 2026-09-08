import React, { createContext, useState, useEffect, useContext } from 'react';
import { fetchCartApi, addToCartApi, updateCartItemApi, removeCartItemApi } from '../api/CartService';

const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(false);

    const refreshCart = async () => {
        try {
            const response = await fetchCartApi();
            setCartItems(response.data.items || []);
        } catch (error) {
            console.error("خطا در دریافت سبد خرید:", error);
        }
    };

    const handleAddToCart = async (product, quantity = 1) => {
        try {
            await addToCartApi(product.id, quantity);
            await refreshCart();
        } catch (error) {
            alert("خطا در افزودن به سبد خرید. احتمالاً موجودی کافی نیست.");
        }
    };

    const handleUpdateQuantity = async (productId, newQuantity) => {
        try {
            if (newQuantity <= 0) {
                await removeCartItemApi(productId);
            } else {
                await updateCartItemApi(productId, newQuantity);
            }
            await refreshCart();
        } catch (error) {
            console.error("خطا در تغییر سبد خرید:", error);
            alert(error.response?.data?.message || "حذف یا تغییر کالا انجام نشد.");
        }
    };

    useEffect(() => {
        if (localStorage.getItem("token")) refreshCart();
    }, []);

    return (
        <CartContext.Provider value={{
            cartItems,
            refreshCart,
            handleAddToCart,
            handleUpdateQuantity,
            cartCount: cartItems.reduce((total, item) => total + item.quantity, 0)
        }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => useContext(CartContext);
