package com.store.onlinestore.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String username;
    private String resetToken;
    private String newPassword;
}
