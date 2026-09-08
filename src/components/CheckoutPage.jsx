import React, { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { checkoutApi } from "../api/CartService";

export default function CheckoutPage() {
    const navigate = useNavigate();
    const { cartItems, refreshCart } = useCart();
    const [form, setForm] = useState({
        fullName: "",
        phone: "",
        city: "",
        address: "",
        postalCode: "",
        notes: ""
    });
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(null);

    const totalPrice = useMemo(
        () => cartItems.reduce((sum, item) => sum + Number(item.price || 0) * Number(item.quantity || 0), 0),
        [cartItems]
    );

    const updateField = (event) => {
        const { name, value } = event.target;
        setForm((previous) => ({ ...previous, [name]: value }));
    };

    const submitOrder = async (event) => {
        event.preventDefault();
        setError("");
        if (!cartItems.length) {
            setError("سبد خرید شما خالی است.");
            return;
        }
        setSubmitting(true);
        try {
            const items = cartItems.map((item) => ({
                productId: item.productId,
                quantity: item.quantity
            }));
            const response = await checkoutApi(items);
            await refreshCart();
            setSuccess(response.data);
        } catch (requestError) {
            setError(requestError.response?.data?.message || "ثبت سفارش انجام نشد. لطفاً موجودی کالاها را بررسی کنید.");
        } finally {
            setSubmitting(false);
        }
    };

    if (success) {
        return (
            <div className="checkout-page">
                <div className="checkout-success">
                    <div className="checkout-success-icon">✓</div>
                    <h2>سفارش شما با موفقیت ثبت شد</h2>
                    <p>شماره سفارش: <strong>{success.orderId}</strong></p>
                    <p>مبلغ قابل پرداخت: <strong>${Number(success.totalPrice || totalPrice).toFixed(2)}</strong></p>
                    <button className="checkout-primary" onClick={() => navigate("/")}>بازگشت به فروشگاه</button>
                </div>
            </div>
        );
    }

    return (
        <div className="checkout-page">
            <div className="checkout-header">
                <h2>نهایی‌سازی خرید</h2>
                <p>اطلاعات ارسال سفارش را وارد کنید</p>
            </div>

            <div className="checkout-layout">
                <form className="checkout-form" onSubmit={submitOrder}>
                    <h3>آدرس و اطلاعات گیرنده</h3>
                    <div className="checkout-fields">
                        <label>نام و نام خانوادگی<input name="fullName" value={form.fullName} onChange={updateField} required /></label>
                        <label>شماره تماس<input name="phone" type="tel" value={form.phone} onChange={updateField} required /></label>
                        <label>شهر<input name="city" value={form.city} onChange={updateField} required /></label>
                        <label>کد پستی<input name="postalCode" inputMode="numeric" value={form.postalCode} onChange={updateField} required /></label>
                        <label className="checkout-field-wide">آدرس کامل<textarea name="address" rows="3" value={form.address} onChange={updateField} required /></label>
                        <label className="checkout-field-wide">توضیحات سفارش (اختیاری)<textarea name="notes" rows="2" value={form.notes} onChange={updateField} /></label>
                    </div>
                    {error && <p className="checkout-error">{error}</p>}
                    <button className="checkout-primary" type="submit" disabled={submitting || !cartItems.length}>
                        {submitting ? "در حال ثبت سفارش..." : "نهایی‌سازی خرید"}
                    </button>
                </form>

                <aside className="checkout-summary">
                    <h3>خلاصه سفارش</h3>
                    <div className="checkout-summary-items">
                        {cartItems.map((item) => (
                            <div className="checkout-summary-item" key={item.productId}>
                                <span>{item.title} × {item.quantity}</span>
                                <strong>${(Number(item.price || 0) * Number(item.quantity || 0)).toFixed(2)}</strong>
                            </div>
                        ))}
                    </div>
                    <div className="checkout-total"><span>مبلغ کل</span><strong>${totalPrice.toFixed(2)}</strong></div>
                    <button type="button" className="checkout-back" onClick={() => navigate("/cart")}>بازگشت به سبد خرید</button>
                </aside>
            </div>
        </div>
    );
}
