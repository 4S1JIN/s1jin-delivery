package com.fourseason.delivery.domain.order.repository;

import com.fourseason.delivery.domain.order.entity.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

  Optional<Order> findByIdAndDeletedAtIsNull(UUID orderId);
}
