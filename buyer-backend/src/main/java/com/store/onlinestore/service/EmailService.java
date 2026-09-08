package com.store.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhreza.moradi@gmail.com"); // حتما ایمیل خودتان را اینجا هم بنویسید
        message.setTo(toEmail);
        message.setSubject("کد تایید بازیابی رمز عبور");
        message.setText("کاربر گرامی،\n\n" +
                "کد تایید شما برای بازیابی رمز عبور: " + otp + "\n" +
                "این کد پس از ۵ دقیقه منقضی می‌شود.\n\n" +
                "اگر شما این درخواست را نداده‌اید، این ایمیل را نادیده بگیرید.");

        mailSender.send(message);
    }
}
