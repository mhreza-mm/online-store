import React from "react";
import { Link } from "react-router-dom";
import FilterBar from "./FilterBar";
import {useCart} from "../context/CartContext";

export default function Navbar({
                                   user,
                                   onLogout,
                                   searchValue,
                                   onSearchChange,
                                   products,
                                   onFilter
                               }) {
    const { cartCount } = useCart();

    return (
        <nav className="navbar-container">

            {/* ردیف بالا */}
            <div className="navbar-top">

                <div className="navbar-right">
                    <Link to="/" className="navbar-logo">market</Link>
                </div>

                <div className="navbar-left">
                    <Link to="/cart" className="navbar-cart">
                        سبد خرید <span>{cartCount}</span>
                    </Link>

                    {user ? (
                        <>
                            <span className="navbar-user">خوش آمدی! | {user.name}</span>
                            <button className="navbar-btn logout" onClick={onLogout}>خروج</button>
                        </>
                    ) : (
                        <Link to="/login" className="navbar-btn login">
                            ورود | ثبت‌نام
                        </Link>
                    )}
                </div>

            </div>

            {/* ردیف دوم */}
            <div className="navbar-bottom">
                <input
                    data-testid="search-input"
                    className="navbar-search"
                    type="text"
                    placeholder="جستجو در محصولات..."
                    value={searchValue}
                    onChange={(e) => onSearchChange(e.target.value)}
                />

                <FilterBar products={products} onFilter={onFilter} />
            </div>

        </nav>
    );
}
