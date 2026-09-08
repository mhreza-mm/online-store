import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

const ResetPassword = () => {
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    const username = location.state?.username; // گرفتن یوزرنیم از مرحله قبل

    const handleSubmit = async (e) => {
        e.preventDefault();

        const resetToken = localStorage.getItem("resetToken");

        if (!resetToken || !username) {
            alert("اطلاعات بازیابی ناقص است");
            return;
        }

        try {
            const res = await fetch("http://localhost:9090/api/auth/reset-password", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username,
                    resetToken,
                    newPassword: password
                })
            });

            const data = await res.json();

            if (!res.ok) {
                alert(data.message || "خطا در تغییر رمز");
                return;
            }

            alert("رمز عبور با موفقیت تغییر کرد");
            localStorage.removeItem("resetToken");
            navigate("/login");

        } catch (err) {
            console.error(err);
            alert("خطا در اتصال به سرور");
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-box">
                <h3>تنظیم رمز جدید برای {username}</h3>
                <form onSubmit={handleSubmit}>
                    <input
                        type="password"
                        placeholder="رمز جدید"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <button type="submit">تغییر رمز</button>
                </form>
            </div>
        </div>
    );
};

export default ResetPassword;
