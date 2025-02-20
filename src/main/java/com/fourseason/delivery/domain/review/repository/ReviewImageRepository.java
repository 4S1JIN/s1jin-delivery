package com.fourseason.delivery.domain.review.repository;

import com.fourseason.delivery.domain.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, UUID> {
    List<ReviewImage> findByReviewIdAndDeletedAtIsNull(UUID reviewId);
}
