import React from "react";
import { useNavigate } from "react-router-dom";

export default function ProductCard({
                                        id,
                                        image,
                                        title,
                                        price,
                                        inCartQuantity,
                                        onAddToCart,
                                        onIncrease,
                                        onDecrease
                                    }) {

    const navigate = useNavigate();

    if (!image) return null;

    const goToDetails = () => {
        navigate(`/product/${id}`);
    };

    return (
        <div className="product-card" onClick={goToDetails}>
            <img src={image} alt={title} />
            <h3 className="product-title">{title}</h3>
            <p className="product-price">${price}</p>

            {inCartQuantity > 0 ? (
                <div className="quantity-controls" onClick={(e)=>e.stopPropagation()}>
                    <button onClick={(e)=>{e.stopPropagation(); onDecrease();}}>-</button>
                    <span>{inCartQuantity}</span>
                    <button onClick={(e)=>{e.stopPropagation(); onIncrease();}}>+</button>
                </div>
            ) : (
                <button
                    className="product-card-add-btn"
                    onClick={(e)=>{
                        e.stopPropagation();
                        onAddToCart();
                    }}
                >
                    افزودن به سبد خرید
                </button>
            )}
        </div>
    );
}
