import React, { useRef, useState } from "react";

const OtpInput = ({ length = 6, onComplete }) => {
    const [otp, setOtp] = useState(new Array(length).fill(""));
    const inputsRef = useRef([]);

    const handleChange = (e, index) => {
        const value = e.target.value;
        if (!/^[0-9]?$/.test(value)) return;

        const newOtp = [...otp];
        newOtp[index] = value;
        setOtp(newOtp);

        // جابجایی خودکار به باکس بعدی
        if (value && index < length - 1) {
            inputsRef.current[index + 1].focus();
        }

        if (newOtp.join("").length === length) {
            onComplete(newOtp.join(""));
        }
    };

    const handleKeyDown = (e, index) => {
        // برگشت به باکس قبلی با دکمه پاک کردن
        if (e.key === "Backspace" && !otp[index] && index > 0) {
            inputsRef.current[index - 1].focus();
        }
    };

    return (
        <div className="otp-input-group">
            {otp.map((digit, index) => (
                <input
                    key={index}
                    type="text"
                    maxLength="1"
                    className="otp-box"
                    value={digit}
                    ref={(el) => (inputsRef.current[index] = el)}
                    onChange={(e) => handleChange(e, index)}
                    onKeyDown={(e) => handleKeyDown(e, index)}
                />
            ))}
        </div>
    );
};

export default OtpInput;
