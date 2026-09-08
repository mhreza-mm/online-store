package com.store.onlinestore.service;

import com.store.onlinestore.entity.*;
import com.store.onlinestore.repository.OrderRepository;
import com.store.onlinestore.repository.ProductRepository;
import com.store.onlinestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.onlinestore.dto.CartItemRequest;
import com.store.onlinestore.dto.CheckoutRequest;
import com.store.onlinestore.dto.CheckoutResponse;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // هماهنگ با AuthService برای تزریق وابستگی‌ها
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CheckoutResponse checkout(String username, CheckoutRequest request) {
        // ۱. پیدا کردن کاربر بر اساس نام کاربری (استخراج شده از توکن)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("کاربر یافت نشد"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("محصولی با کد " + itemRequest.getProductId() + " یافت نشد"));

            // ۲. بررسی موجودی انبار
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("موجودی کافی برای محصول " + product.getTitle() + " وجود ندارد");
            }

            // ۳. ایجاد آیتم سفارش
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);

            // ۴. بروزرسانی انبار
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            totalAmount += product.getPrice() * itemRequest.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return CheckoutResponse.builder()
                .orderId(savedOrder.getId())
                .totalPrice(savedOrder.getTotalPrice())
                .message("سفارش شما با موفقیت ثبت شد")
                .build();
    }
}
