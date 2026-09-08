import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchProductById } from "../api/Products";

export default function ProductDetails(
    {
        onAddToCart,
        onIncrease,
        onDecrease,
        inCartQuantity,
        getInCartQuantity
    }
) {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadProduct = async () => {
            try {
                setLoading(true);
                const data = await fetchProductById(id);
                setProduct(data);
            } catch (err) {
                console.error("Error loading product:", err);
            } finally {
                setLoading(false);
            }
        };
        loadProduct();
    }, [id]);

    if (loading) {
        return <div style={{ padding: "40px", textAlign: "center" }}>در حال بارگذاری...</div>;
    }

    if (!product) {
        return <div style={{ padding: "40px", textAlign: "center" }}>محصول مورد نظر یافت نشد.</div>;
    }

    const currentQuantity = getInCartQuantity
        ? getInCartQuantity(product.id)
        : (inCartQuantity || 0);

    return (
        <div className="product-details-container">
            <div className="product-details-card">
                <img
                    src={product.image}
                    alt={product.title}
                    className="product-details-image"
                />

                <div className="product-details-info">
                    <h2 className="product-title">{product.title}</h2>
                    <p className="product-price">${product.price}</p>
                    <p className="product-description">
                        {product.description || "توضیحاتی برای این محصول ثبت نشده است."}
                    </p>

                    {currentQuantity > 0 ? (
                        <div className="quantity-controls">
                            <button onClick={() => onDecrease(product.id)}>-</button>
                            <span>{currentQuantity}</span>
                            <button onClick={() => onIncrease(product.id)}>+</button>
                        </div>
                    ) : (
                        <button
                            className="product-card-add-btn"
                            onClick={() => onAddToCart(product)}
                        >
                            افزودن به سبد خرید
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}
