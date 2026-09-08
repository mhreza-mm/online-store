package com.store.onlinestore.service;

import com.store.onlinestore.dto.*;
import com.store.onlinestore.entity.PasswordResetRequest;
import com.store.onlinestore.entity.User;
import com.store.onlinestore.entity.User.Role;
import com.store.onlinestore.repository.PasswordResetRequestRepository;
import com.store.onlinestore.repository.UserRepository;
import com.store.onlinestore.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetRequestRepository passwordResetRequestRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // =========================
    // REGISTER
    // =========================
    public ResponseEntity<AuthResponse> register(LoginRequest request) {

        String username = request.getUsername().trim();
        String email = request.getEmail().trim();

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    AuthResponse.builder()
                            .message("این نام کاربری قبلاً ثبت شده است")
                            .build()
            );
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(email);
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateJwtToken(savedUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AuthResponse.builder()
                        .message("ثبت‌نام موفقیت‌آمیز بود")
                        .userId(savedUser.getId())
                        .username(savedUser.getUsername())
                        .token(token)
                        .build()
        );
    }

    // =========================
    // LOGIN
    // =========================
    public ResponseEntity<AuthResponse> login(LoginRequest request) {

        String username = request.getUsername().trim();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("کاربری با این نام یافت نشد"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    AuthResponse.builder()
                            .message("رمز عبور اشتباه است")
                            .build()
            );
        }

        String token = jwtUtil.generateJwtToken(user.getUsername());

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .message("ورود موفقیت‌آمیز")
                        .userId(user.getId())
                        .username(user.getUsername())
                        .token(token)
                        .build()
        );
    }

    // =========================
    // STEP 1: SEND OTP
    // =========================
    public ResponseEntity<AuthResponse> sendOtp(ForgotPasswordRequest request) {

        String username = request.getUsername().trim();
        String email = request.getEmail().trim();

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    AuthResponse.builder()
                            .message("کاربر یافت نشد")
                            .build()
            );
        }

        if (!user.getEmail().trim().equalsIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    AuthResponse.builder()
                            .message("ایمیل با نام کاربری مطابقت ندارد")
                            .build()
            );
        }

        // غیرفعال کردن درخواست‌های قبلی
        List<PasswordResetRequest> oldRequests =
                passwordResetRequestRepository.findAllActiveRequestsByUser(user);

        oldRequests.forEach(r -> r.setUsed(true));
        passwordResetRequestRepository.saveAll(oldRequests);

        // تولید OTP
        String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));

        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setUser(user);
        resetRequest.setOtp(otp);
        resetRequest.setOtpVerified(false);
        resetRequest.setUsed(false);
        resetRequest.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));

        passwordResetRequestRepository.save(resetRequest);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .message("کد تایید به ایمیل ارسال شد")
                        .build()
        );
    }


    // =========================
    // STEP 2: VERIFY OTP
    // =========================
    public ResponseEntity<AuthResponse> verifyOtp(VerifyOtpRequest request) {

        String username = request.getUsername().trim();
        String otp = request.getOtp().trim();

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AuthResponse.builder().message("کاربر یافت نشد").build());
        }

        List<PasswordResetRequest> requests =
                passwordResetRequestRepository
                        .findActiveOtpRequests(user, otp);

        if (requests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.builder().message("کد تایید نامعتبر است").build());
        }

        PasswordResetRequest resetRequest = requests.get(0);

        if (resetRequest.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.builder().message("کد تایید منقضی شده است").build());
        }

        resetRequest.setOtpVerified(true);

        String resetToken = UUID.randomUUID().toString();
        resetRequest.setResetToken(resetToken);
        resetRequest.setResetTokenExpiryTime(LocalDateTime.now().plusMinutes(15));

        passwordResetRequestRepository.save(resetRequest);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .message("کد تایید با موفقیت تایید شد")
                        .resetToken(resetToken)
                        .build()
        );
    }


    // =========================
    // STEP 3: RESET PASSWORD
    // =========================
    public ResponseEntity<AuthResponse> resetPassword(ResetPasswordRequest request) {

        String username = request.getUsername().trim();
        String resetToken = request.getResetToken().trim();

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AuthResponse.builder().message("کاربر یافت نشد").build());
        }

        Optional<PasswordResetRequest> optional =
                passwordResetRequestRepository
                        .findByUserAndResetTokenAndUsedFalse(user, resetToken);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.builder().message("توکن معتبر نیست").build());
        }

        PasswordResetRequest resetRequest = optional.get();

        if (!resetRequest.isOtpVerified()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.builder().message("ابتدا کد تایید را تایید کنید").build());
        }

        if (resetRequest.getResetTokenExpiryTime() == null ||
                resetRequest.getResetTokenExpiryTime().isBefore(LocalDateTime.now())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.builder().message("توکن بازیابی منقضی شده است").build());
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetRequest.setUsed(true);
        passwordResetRequestRepository.save(resetRequest);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .message("رمز عبور با موفقیت تغییر یافت")
                        .build()
        );
    }

}
