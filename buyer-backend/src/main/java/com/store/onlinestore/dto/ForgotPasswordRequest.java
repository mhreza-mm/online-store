package com.store.onlinestore.dto;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String username;
    private String email;
}
