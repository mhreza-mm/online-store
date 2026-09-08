import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const ForgotPassword = () => {

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!username || !email) {
            alert("نام کاربری و ایمیل را وارد کنید");
            return;
        }

        try {

            const res = await fetch("http://localhost:9090/api/auth/forgot-password", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username,
                    email
                })
            });

            const data = await res.json();

            if (!res.ok) {
                alert(data.message || "خطا در ارسال کد");
                return;
            }

            alert("کد تایید به ایمیل شما ارسال شد");

            navigate("/verify-otp", {
                state: { username,email }
            });

        } catch (err) {
            console.error(err);
            alert("خطا در اتصال به سرور");
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-box">

                <h3>بازیابی رمز عبور</h3>

                <form onSubmit={handleSubmit}>

                    <input
                        type="text"
                        placeholder="نام کاربری"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    <input
                        type="email"
                        placeholder="ایمیل"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <button type="submit">
                        ارسال کد تایید
                    </button>

                </form>

            </div>
        </div>
    );
};

export default ForgotPassword;
