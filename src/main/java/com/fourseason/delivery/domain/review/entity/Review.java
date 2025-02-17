package com.fourseason.delivery.domain.review.entity;

import com.fourseason.delivery.domain.member.entity.Member;
import com.fourseason.delivery.domain.order.entity.Order;
import com.fourseason.delivery.domain.review.dto.request.ReviewRequestDto;
import com.fourseason.delivery.domain.shop.entity.Shop;
import com.fourseason.delivery.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @UuidGenerator
    private UUID id;

    private String content;

    @Column(nullable = false)
    private int rating;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Builder
    public Review(String content, int rating, Member member, Order order, Shop shop) {
        this.content = content;
        this.rating = rating;
        this.member = member;
        this.order = order;
        this.shop = shop;
    }


    public static Review addOf(ReviewRequestDto dto, Order order) {
        return Review.builder()
                .content(dto.getContent())
                .rating(dto.getRating())
                .order(order)
                .member(order.getMember())
                .shop(order.getShop())
                .build();
    }

    public void updateOf(ReviewRequestDto dto) {
        this.content = dto.getContent();
        this.rating = dto.getRating();
    }
}
