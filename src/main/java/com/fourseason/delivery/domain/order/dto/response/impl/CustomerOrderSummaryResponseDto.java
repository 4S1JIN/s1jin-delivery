package com.fourseason.delivery.domain.order.dto.response.impl;

import com.fourseason.delivery.domain.order.dto.response.OrderSummaryResponseDto;
import com.fourseason.delivery.domain.order.entity.Order;
import com.fourseason.delivery.domain.order.entity.OrderMenu;
import com.fourseason.delivery.domain.order.entity.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record CustomerOrderSummaryResponseDto(
    String shopName,
    String address,
    Integer totalPrice,
    OrderStatus status,
    List<MenuDto> menuList,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String updatedBy
) implements OrderSummaryResponseDto {

  @QueryProjection
  public CustomerOrderSummaryResponseDto(Order order) {
    this(order.getShop().getName(),
        order.getAddress(),
        order.getTotalPrice(),
        order.getOrderStatus(),
        order.getOrderMenuList().stream().map(MenuDto::of).toList(),
        order.getCreatedAt(),
        order.getUpdatedAt(),
        order.getUpdatedBy()
    );
  }

  @Builder
  public record MenuDto(
      String name,
      Integer quantity
  ) {

    public static MenuDto of(OrderMenu orderMenu) {
      return MenuDto.builder()
          .name(orderMenu.getName())
          .quantity(orderMenu.getQuantity())
          .build();
    }
  }
}
