import React from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";

export default function CartPage() {
    const { cartItems, handleUpdateQuantity } = useCart();
    const navigate = useNavigate();

    if (cartItems.length === 0) {
        return <div className="cart-page"><h2>سبد خرید</h2><p>سبد خرید شما خالی است.</p></div>;
    }

    const totalPrice = cartItems.reduce((sum, item) => sum + Number(item.price || 0) * Number(item.quantity || 0), 0);

    return (
        <div className="cart-page">
            <h2>سبد خرید</h2>
            <div className="cart-items">
                {cartItems.map((item) => (
                    <div className="cart-item" key={item.productId}>
                        <img src={item.image} alt={item.title} className="cart-item-image" />
                        <div className="cart-item-info">
                            <h3>{item.title}</h3>
                            <button
                                type="button"
                                className="cart-remove-btn"
                                onClick={() => handleUpdateQuantity(item.productId, 0)}
                            >
                                حذف کالا
                            </button>
                            <div className="cart-quantity">
                                <button onClick={() => handleUpdateQuantity(item.productId, item.quantity - 1)}>-</button>
                                <span>{item.quantity}</span>
                                <button onClick={() => handleUpdateQuantity(item.productId, item.quantity + 1)}>+</button>
                            </div>
                            <p>قیمت واحد: ${item.price}</p>
                            <p>مجموع: ${(Number(item.price || 0) * item.quantity).toFixed(2)}</p>
                        </div>
                    </div>
                ))}
            </div>
            <div className="cart-summary">
                <h3>جمع کل خرید: ${totalPrice.toFixed(2)}</h3>
                <button className="checkout-btn" onClick={() => navigate("/checkout")}>نهایی‌سازی خرید</button>
            </div>
        </div>
    );
}
