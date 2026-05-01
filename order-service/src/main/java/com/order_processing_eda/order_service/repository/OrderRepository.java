package com.order_processing_eda.order_service.repository;

import com.order_processing_eda.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    boolean existsByIdempotencyKey(String key);
}
