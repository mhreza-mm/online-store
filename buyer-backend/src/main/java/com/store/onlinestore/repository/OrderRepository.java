package com.store.onlinestore.repository;

import com.store.onlinestore.entity.Order;
import com.store.onlinestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findByUserId(Long userId);
}
