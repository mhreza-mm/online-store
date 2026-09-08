package com.store.onlinestore.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String username;
    private String email;
    private String otp;
}
