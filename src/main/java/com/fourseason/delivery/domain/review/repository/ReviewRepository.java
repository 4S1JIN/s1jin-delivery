package com.fourseason.delivery.domain.review.repository;

import com.fourseason.delivery.domain.review.entity.Review;
import com.fourseason.delivery.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findByIdAndOrderIdAndDeletedAtIsNull(UUID reviewId, UUID orderId);

    List<Review> findByShopAndDeletedAtIsNull(Shop shop);
}
