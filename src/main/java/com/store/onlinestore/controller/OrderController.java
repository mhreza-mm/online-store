package com.store.onlinestore.controller;

import com.store.onlinestore.dto.CheckoutRequest;
import com.store.onlinestore.dto.CheckoutResponse;
import com.store.onlinestore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @RequestBody CheckoutRequest request,
            Principal principal
    ) {
        // نام کاربری را از Principal (امنیت JWT) می‌گیریم
        String username = principal.getName();

        // ارسال مستقیم به سرویس
        CheckoutResponse response = orderService.checkout(username, request);

        return ResponseEntity.ok(response);
    }
}
