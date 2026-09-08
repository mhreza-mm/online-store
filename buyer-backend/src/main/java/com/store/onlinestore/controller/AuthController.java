package com.store.onlinestore.controller;

import com.store.onlinestore.dto.*;
import com.store.onlinestore.entity.User;
import com.store.onlinestore.repository.UserRepository;
import com.store.onlinestore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;


    @GetMapping("/count")
    public Long getUserCount(){
        return userRepository.count();
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return
    userRepository.findAll();
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // =========================
    // STEP 1: SEND OTP
    // =========================
    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        return authService.sendOtp(request);
    }

    // =========================
    // STEP 2: VERIFY OTP
    // =========================
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(
            @RequestBody VerifyOtpRequest request) {

        return authService.verifyOtp(request);
    }

    // =========================
    // STEP 3: RESET PASSWORD
    // =========================
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        return authService.resetPassword(request);
    }

}
