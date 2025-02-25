package com.fourseason.delivery.domain.order.dto.request;

import static com.fourseason.delivery.domain.member.entity.Role.MANAGER;

import com.fourseason.delivery.domain.member.entity.Member;
import com.fourseason.delivery.domain.order.dto.request.OrderCreateDto.OrderMenuCreateDto;
import com.fourseason.delivery.domain.order.entity.OrderStatus;
import com.fourseason.delivery.domain.order.entity.OrderType;
import com.fourseason.delivery.domain.shop.entity.Shop;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ManagerCreateOrderRequestDto(
    @NotNull(message = "가게 id는 필수 입력 값입니다.")
    UUID shopId,

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    String address,

    @NotEmpty(message = "주문 상품이 없습니다.")
    List<MenuDto> menuList,

    String instruction,

    @NotNull(message = "주문 상태는 필수 입력 값입니다.")
    OrderStatus orderStatus,

    @NotNull(message = "주문 유형은 필수 입력 값입니다.")
    OrderType orderType,

    Long customerId
) {

  @Builder
  public record MenuDto(
      @NotNull(message = "메뉴 id는 필수 입력 값입니다.") UUID menuId,
      @Positive(message = "메뉴 수량은 양수여야합니다.") int quantity) {

    public OrderMenuCreateDto toOrderMenuCreateDto() {
      return OrderMenuCreateDto.builder()
          .menuId(this.menuId)
          .quantity(this.quantity)
          .build();
    }
  }

  public OrderCreateDto toOrderCreateDto(Shop shop, Member customer) {
    return OrderCreateDto.builder()
        .orderMenuCreateDtoList(this.menuList.stream().map(MenuDto::toOrderMenuCreateDto).toList())
        .byRole(MANAGER)
        .shop(shop)
        .customer(customer)
        .address(this.address)
        .instruction(this.instruction)
        .status(this.orderStatus)
        .type(this.orderType)
        .build();
  }
}
