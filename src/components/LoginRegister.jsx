import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const LoginRegister = ({ onLogin }) => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [isLoginMode, setIsLoginMode] = useState(true);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!username || !password) {
            alert("نام کاربری و رمز عبور را وارد کنید");
            return;
        }

        if (!isLoginMode && !email) {
            alert("ایمیل را وارد کنید");
            return;
        }

        const endpoint = isLoginMode ? "login" : "register";
        const apiUrl = `http://localhost:9090/api/auth/${endpoint}`;

        const requestBody = isLoginMode
            ? { username, password }
            : { username, password, email };

        try {
            setLoading(true);

            const res = await fetch(apiUrl, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody),
            });

            let data = {};
            try {
                data = await res.json();
            } catch (err) {
                console.warn("Response is not JSON");
            }

            if (!res.ok) {
                alert(data.message || "خطا در عملیات");
                return;
            }

            const loggedInUser = data.username || username;

            if (data.token) {
                localStorage.setItem("token", data.token);
            }

            localStorage.setItem("username", loggedInUser);

            alert(data.message || "عملیات موفقیت‌آمیز");

            onLogin({ name: loggedInUser });
            navigate("/");

        } catch (err) {
            console.error("خطای اتصال:", err);
            alert("خطا در اتصال به سرور (CORS یا شبکه)");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-box">
                <h3 className="login-title">
                    {isLoginMode ? "ورود به حساب" : "ثبت‌نام در سایت"}
                </h3>

                <form onSubmit={handleSubmit}>

                    <input
                        type="text"
                        placeholder="نام کاربری"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    {!isLoginMode && (
                        <input
                            type="email"
                            placeholder="ایمیل"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    )}

                    <input
                        type="password"
                        placeholder="رمز عبور"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <button type="submit" className="login-btn" disabled={loading}>
                        {loading
                            ? "در حال پردازش..."
                            : isLoginMode
                                ? "ورود"
                                : "ثبت‌نام"}
                    </button>

                </form>

                {/* ✅ لینک فراموشی رمز */}
                {isLoginMode && (
                    <p
                        style={{
                            marginTop: "10px",
                            cursor: "pointer",
                            color: "#dc3545"
                        }}
                        onClick={() => navigate("/forgot-password")}
                    >
                        رمز عبور را فراموش کرده‌اید؟
                    </p>
                )}

                <p
                    className="switch-mode"
                    onClick={() => {
                        setIsLoginMode(!isLoginMode);
                        setEmail("");
                    }}
                    style={{ cursor: "pointer", marginTop: "10px", color: "#007bff" }}
                >
                    {isLoginMode
                        ? "هنوز ثبت‌نام نکرده‌اید؟ ثبت‌نام کنید"
                        : "قبلاً ثبت‌نام کرده‌اید؟ وارد شوید"}
                </p>
            </div>
        </div>
    );
};

export default LoginRegister;
