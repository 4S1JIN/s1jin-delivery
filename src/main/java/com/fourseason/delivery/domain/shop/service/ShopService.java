package com.fourseason.delivery.domain.shop.service;

import com.fourseason.delivery.domain.image.enums.S3Folder;
import com.fourseason.delivery.domain.image.service.FileService;
import com.fourseason.delivery.domain.member.entity.Member;
import com.fourseason.delivery.domain.member.exception.MemberErrorCode;
import com.fourseason.delivery.domain.member.repository.MemberRepository;
import com.fourseason.delivery.domain.shop.dto.request.CreateShopRequestDto;
import com.fourseason.delivery.domain.shop.dto.request.UpdateShopRequestDto;
import com.fourseason.delivery.domain.shop.dto.response.ShopResponseDto;
import com.fourseason.delivery.domain.shop.entity.Category;
import com.fourseason.delivery.domain.shop.entity.Shop;
import com.fourseason.delivery.domain.shop.entity.ShopImage;
import com.fourseason.delivery.domain.shop.exception.ShopErrorCode;
import com.fourseason.delivery.domain.shop.repository.CategoryRepository;
import com.fourseason.delivery.domain.shop.repository.ShopImageRepository;
import com.fourseason.delivery.domain.shop.repository.ShopRepository;
import com.fourseason.delivery.domain.shop.repository.ShopRepositoryCustom;
import com.fourseason.delivery.global.dto.PageRequestDto;
import com.fourseason.delivery.global.dto.PageResponseDto;
import com.fourseason.delivery.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    private final ShopRepositoryCustom shopRepositoryCustom;

    private final ShopImageRepository shopImageRepository;

    private final CategoryRepository categoryRepository;

    private final MemberRepository memberRepository;

    private final FileService fileService;

    @Transactional(readOnly = true)
    public PageResponseDto<ShopResponseDto> getShopList(PageRequestDto pageRequestDto) {
        return shopRepositoryCustom.findShopListWithPage(pageRequestDto);
    }

    @Transactional(readOnly = true)
    public ShopResponseDto getShop(final UUID id) {
        Shop shop = shopRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new CustomException(ShopErrorCode.SHOP_NOT_FOUND));

        List<String> images = shopImageRepository.findByShopId(id)
            .stream()
            .map(ShopImage::getImageUrl)
            .toList();

        return ShopResponseDto.of(shop, images);
    }

    @Transactional
    public void registerShop(CreateShopRequestDto createShopRequestDto, List<MultipartFile> images, final Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Category category = categoryRepository.findByName(createShopRequestDto.category())
            .orElseThrow(() -> new CustomException(ShopErrorCode.CATEGORY_NOT_FOUND));

        Shop shop = Shop.addOf(createShopRequestDto, member, category);
        shopRepository.save(shop);

        for (MultipartFile file : images) {
            fileService.saveImageFile(S3Folder.SHOP, file, shop.getId());
        }
    }

    @Transactional
    public void updateShop(final UUID id, UpdateShopRequestDto updateShopRequestDto) {
        Shop shop = shopRepository.findById(id)
            .orElseThrow(() -> new CustomException(ShopErrorCode.SHOP_NOT_FOUND));

        Category category = categoryRepository.findByName(updateShopRequestDto.category())
            .orElseThrow(() -> new CustomException(ShopErrorCode.CATEGORY_NOT_FOUND));

        shop.updateOf(updateShopRequestDto, category);
    }

    @Transactional
    public void deleteShop(final UUID id, final Long memberId) {
        Shop shop = shopRepository.findById(id)
            .orElseThrow(() -> new CustomException(ShopErrorCode.SHOP_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        shop.deleteOf(member.getUsername());
    }

    @Transactional
    public PageResponseDto<ShopResponseDto> searchShop(PageRequestDto pageRequestDto, String keyword) {
        return shopRepositoryCustom.searchShopWithPage(pageRequestDto, keyword);
    }
}
