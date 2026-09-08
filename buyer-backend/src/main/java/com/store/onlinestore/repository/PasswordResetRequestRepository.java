package com.store.onlinestore.repository;

import com.store.onlinestore.entity.PasswordResetRequest;
import com.store.onlinestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, Long> {

    @Query("SELECT p FROM PasswordResetRequest p " +
            "WHERE p.user = :user " +
            "AND p.otp = :otp " +
            "AND p.used = false " +
            "ORDER BY p.id DESC")
    List<PasswordResetRequest> findActiveOtpRequests(
            @Param("user") User user,
            @Param("otp") String otp
    );

    @Query("SELECT p FROM PasswordResetRequest p " +
            "WHERE p.user = :user " +
            "AND p.resetToken = :resetToken " +
            "AND p.used = false")
    Optional<PasswordResetRequest> findByUserAndResetTokenAndUsedFalse(
            @Param("user") User user,
            @Param("resetToken") String resetToken
    );

    @Query("SELECT p FROM PasswordResetRequest p " +
            "WHERE p.user = :user " +
            "AND p.used = false")
    List<PasswordResetRequest> findAllActiveRequestsByUser(
            @Param("user") User user
    );
}
