import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import OtpInput from "../components/OtpInput";

const VerifyOtp = () => {
    const location = useLocation();
    const navigate = useNavigate();

    // گرفتن اطلاعات از صفحه قبل
    const username = location.state?.username;
    const email = location.state?.email;

    const handleVerify = async (otpValue) => {
        // اگر یوزرنیم یا ایمیل در استیت نبود، اجازه ادامه نده
        if (!username || !email) {
            alert("اطلاعات کاربر یافت نشد. لطفا دوباره تلاش کنید.");
            navigate("/forgot-password");
            return;
        }

        try {
            const res = await fetch("http://localhost:9090/api/auth/verify-otp", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username,
                    email, // ✅ حالا ایمیل ارسال می‌شود و بک‌اِند خطا نمی‌دهد
                    otp: otpValue
                })
            });

            const data = await res.json();

            if (!res.ok) {
                alert(data.message || "کد تایید اشتباه است");
                return;
            }

            // ذخیره توکن برای مرحله بعد
            localStorage.setItem("resetToken", data.resetToken);
            // فرستادن یوزرنیم به مرحله بعد چون بک‌اِند برای ResetPassword به آن نیاز دارد
            navigate("/reset-password", { state: { username } });

        } catch (err) {
            console.error(err);
            alert("خطا در اتصال به سرور");
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-box">
                <h3>کد تایید را وارد کنید</h3>
                <p style={{ fontSize: '12px', color: '#aaa' }}>کد به ایمیل {email} ارسال شد</p>

                <OtpInput
                    length={6}
                    onComplete={(value) => {
                        handleVerify(value); // ✅ مقدار OTP مستقیم به تابع پاس داده می‌شود
                    }}
                />
            </div>
        </div>
    );
};

export default VerifyOtp;
