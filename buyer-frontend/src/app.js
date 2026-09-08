import React, { useState, useEffect } from "react";
import Navbar from "./components/Navbar";
import LoginRegister from "./components/LoginRegister";
import UserPanel from "./components/UserPanel";
import ProductList from "./components/ProductList";
import Pagination from "./components/Pagination";
import CartPage from "./components/CartPage";
import CheckoutPage from "./components/CheckoutPage";
import ProductDetails from "./components/ProductDetails";
import { Routes, Route, Navigate, useNavigate } from "react-router-dom";
import { fetchProducts } from "./api/Products";
import bannerImg from "./assets/baner.png";
import ResetPassword from "./components/ResetPassword";
import VerifyOtp from "./components/VerifyOtp";
import ForgotPassword from "./components/ForgotPassword";

// وارد کردن Context و Provider سبد خرید
import { CartProvider, useCart } from "./context/CartContext";

const ProtectedRoute = ({ children }) => {
    const token = localStorage.getItem("token");
    return token ? children : <Navigate to="/login" />;
};

const AdminRoute = ({ children }) => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");
    return token && role === "ADMIN" ? children : <Navigate to="/login" />;
};

// جدا کردن محتوای اصلی برای دسترسی به هوک useCart
function AppContent({ user, setUser, products, setPage, page, totalPages, searchValue, setSearchValue, setSelectedType, setSelectedBrand, loadProducts }) {
    const navigate = useNavigate();

    const { cartItems, cartCount, handleAddToCart, handleUpdateQuantity, refreshCart } = useCart();

    const onLogout = () => {
        setUser(null);
        localStorage.clear();
        window.location.href = "/"; // رفرش برای پاکسازی استیت‌ها
    };

    // تابع کمکی برای پیدا کردن تعداد موجود در سبد برای هر محصول
    const getInCartQuantity = (productId) => {
        const item = cartItems.find(i => i.productId === productId);
        return item ? item.quantity : 0;
    };

    return (
        <div className="app-container">
            <Navbar
                user={user}
                onLogin={setUser}
                searchValue={searchValue}
                onSearchChange={setSearchValue}
                onLogout={onLogout}
                products={products}
                onFilter={(type, brand) => {
                    setSelectedType(type);
                    setSelectedBrand(brand);
                    setPage(0);
                }}
            />

            <main className="main-content">
                <Routes>
                    <Route path="/" element={
                        <>
                            <div className="homepage-banner">
                                <img src={bannerImg} alt={"banner"}/>
                            </div>
                            <ProductList
                                products={products.map(p => ({
                                    ...p,
                                    inCartQuantity: getInCartQuantity(p.id)
                                }))}
                                onAddToCart={(product) => {
                                    if(!user) return navigate("/login");
                                    handleAddToCart(product);
                                }}
                                onIncrease={(id) => handleUpdateQuantity(id, getInCartQuantity(id) + 1)}
                                onDecrease={(id) => handleUpdateQuantity(id, getInCartQuantity(id) - 1)}
                            />
                            <Pagination
                                page={page + 1}
                                totalPages={totalPages}
                                onPageChange={(p) => setPage(p - 1)}
                            />
                        </>
                    } />

                    <Route path="/product/:id" element={
                        <ProductDetails
                            getInCartQuantity={getInCartQuantity}
                            onAddToCart={(product) => {
                                if(!user) return navigate("/login");
                                handleAddToCart(product);
                            }}
                            onIncrease={(id) => handleUpdateQuantity(id, getInCartQuantity(id) + 1)}
                            onDecrease={(id) => handleUpdateQuantity(id, getInCartQuantity(id) - 1)}
                        />
                    } />

                    <Route path="/login" element={<LoginRegister onLogin={setUser} />} />
                    <Route path="/forgot-password" element={<ForgotPassword />} />
                    <Route path="/verify-otp" element={<VerifyOtp />} />
                    <Route path="/reset-password" element={<ResetPassword />} />

                    <Route path="/user" element={
                        <ProtectedRoute>
                            <UserPanel user={user} onLogout={onLogout} />
                        </ProtectedRoute>
                    } />

                    <Route path="/cart" element={
                        <ProtectedRoute>
                            <CartPage />
                        </ProtectedRoute>
                    } />

                    <Route path="/checkout" element={
                        <ProtectedRoute>
                            <CheckoutPage />
                        </ProtectedRoute>
                    } />
                </Routes>
            </main>
        </div>
    );
}

// کامپوننت اصلی که Provider را دور برنامه می‌پیچد
export default function App() {
    const [user, setUser] = useState(null);
    const [products, setProducts] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [selectedType, setSelectedType] = useState("");
    const [selectedBrand, setSelectedBrand] = useState("");
    const [searchValue, setSearchValue] = useState("");
    const pageSize = 10;

    useEffect(() => {
        const username = localStorage.getItem("username");
        const role = localStorage.getItem("role");
        if (username) setUser({ name: username, role });
    }, []);

    const loadProducts = async () => {
        try {
            const data = await fetchProducts({
                page,
                size: pageSize,
                type: selectedType,
                brand: selectedBrand,
                search: searchValue
            });
            setProducts(data.content);
            setTotalPages(data.totalPages);
        } catch (err) {
            console.error("خطا در دریافت محصولات", err);
        }
    };

    useEffect(() => {
        loadProducts();
    }, [page, selectedType, selectedBrand, searchValue]);

    return (
        <CartProvider>
            <AppContent
                user={user} setUser={setUser}
                products={products} setPage={setPage}
                page={page} totalPages={totalPages}
                searchValue={searchValue} setSearchValue={setSearchValue}
                setSelectedType={setSelectedType} setSelectedBrand={setSelectedBrand}
                loadProducts={loadProducts}
            />
        </CartProvider>
    );
}
