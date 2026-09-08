package com.store.onlinestore.controller;

import com.store.onlinestore.dto.*;
import com.store.onlinestore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication auth) {

        return ResponseEntity.ok(
                cartService.getCart(auth.getName())
        );
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            Authentication auth,
            @RequestBody AddToCartRequest request
    ) {

        return ResponseEntity.ok(
                cartService.addToCart(auth.getName(), request)
        );
    }

    @PutMapping("/items")
    public ResponseEntity<CartResponse> updateCartItem(
            Authentication auth,
            @RequestBody AddToCartRequest request
    ) {
        return ResponseEntity.ok(cartService.updateCartItem(auth.getName(), request));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeCartItem(
            Authentication auth,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(cartService.removeCartItem(auth.getName(), productId));
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            Authentication auth
    ) {

        return ResponseEntity.ok(
                cartService.checkoutCart(auth.getName())
        );
    }
}
