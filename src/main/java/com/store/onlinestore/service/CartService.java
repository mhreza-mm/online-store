package com.store.onlinestore.service;

import com.store.onlinestore.dto.*;
import com.store.onlinestore.entity.*;
import com.store.onlinestore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public CartResponse getCart(String username) {

        Cart cart = getOrCreateCart(username);

        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse addToCart(String username, AddToCartRequest request) {

        if (request == null || request.getProductId() == null || request.getQuantity() == null || request.getQuantity() <= 0)
            throw new RuntimeException("اطلاعات افزودن کالا نامعتبر است");

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("محصول یافت نشد"));

        Cart cart = getOrCreateCart(username);

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        int requested = request.getQuantity();

        if (cartItem != null)
            requested += cartItem.getQuantity();

        if (requested > product.getStock())
            throw new RuntimeException("موجودی کافی وجود ندارد");

        if (cartItem == null) {

            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();

        } else {

            cartItem.setQuantity(requested);
        }

        cartItemRepository.save(cartItem);

        return mapToResponse(getOrCreateCart(username));
    }

    @Transactional
    public CartResponse updateCartItem(String username, AddToCartRequest request) {
        if (request == null || request.getProductId() == null || request.getQuantity() == null || request.getQuantity() <= 0)
            throw new RuntimeException("تعداد کالا باید بیشتر از صفر باشد");

        Cart cart = getOrCreateCart(username);
        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElseThrow(() -> new RuntimeException("کالا در سبد خرید یافت نشد"));
        if (request.getQuantity() > item.getProduct().getStock())
            throw new RuntimeException("موجودی کافی وجود ندارد");
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse removeCartItem(String username, Long productId) {
        Cart cart = getOrCreateCart(username);
        cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .ifPresent(item -> {
                    // Keep the in-memory relationship in sync with the database.
                    cart.getItems().remove(item);
                    cartItemRepository.delete(item);
                });
        return mapToResponse(cart);
    }

    @Transactional
    public CheckoutResponse checkoutCart(String username) {

        Cart cart = getOrCreateCart(username);

        if (cart.getItems().isEmpty())
            throw new RuntimeException("سبد خرید خالی است");

        CheckoutRequest request = new CheckoutRequest();

        List<CartItemRequest> items = new ArrayList<>();

        for (CartItem item : cart.getItems()) {

            items.add(new CartItemRequest(
                    item.getProduct().getId(),
                    item.getQuantity()
            ));
        }

        request.setItems(items);

        CheckoutResponse response =
                orderService.checkout(username, request);

        cartItemRepository.deleteAll(cart.getItems());

        return response;
    }

    private Cart getOrCreateCart(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("کاربر یافت نشد"));

        return cartRepository
                .findByUserUsername(username)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));
    }

    private CartResponse mapToResponse(Cart cart) {

        List<CartItemResponse> items = new ArrayList<>();

        double total = 0;

        for (CartItem item : cart.getItems()) {

            Product p = item.getProduct();

            double subtotal = p.getPrice() * item.getQuantity();

            items.add(new CartItemResponse(
                    item.getId(),
                    p.getId(),
                    p.getTitle(),
                    p.getImage(),
                    p.getPrice(),
                    item.getQuantity(),
                    subtotal,
                    p.getStock()
            ));

            total += subtotal;
        }

        return new CartResponse(
                cart.getId(),
                items,
                total,
                items.size()
        );
    }
}
